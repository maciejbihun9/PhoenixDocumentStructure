package com.volvo.phoenix.document.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.volvo.phoenix.document.datatype.ItemType;
import com.volvo.phoenix.document.datatype.NodeType;
import com.volvo.phoenix.document.datatype.NotificationState;
import com.volvo.phoenix.document.datatype.OperationStatus;
import com.volvo.phoenix.document.datatype.OperationType;
import com.volvo.phoenix.document.dto.DocumentAuditLogDTO;
import com.volvo.phoenix.document.dto.FolderAuditLogDTO;
import com.volvo.phoenix.document.dto.TreeNodeDTO;
import com.volvo.phoenix.document.entity.Document;
import com.volvo.phoenix.document.entity.Operation;
import com.volvo.phoenix.document.entity.SolutionParam;
import com.volvo.phoenix.document.repository.DocumentRepository;
import com.volvo.phoenix.document.repository.FolderRepository;
import com.volvo.phoenix.document.repository.SolutionParamRepository;
import com.volvo.phoenix.document.service.AuditLogService;
import com.volvo.phoenix.document.service.DocumentStructureService;
import com.volvo.phoenix.document.service.LdapUserService;
import com.volvo.phoenix.document.service.OperationService;
import com.volvo.phoenix.document.translator.NodeResolver;
import com.volvo.phoenix.document.translator.OperationTranslator;
import com.volvo.phoenix.orion.entity.OrionDocument;
import com.volvo.phoenix.orion.repository.OrionDocumentRepository;
import com.volvo.phoenix.orion.service.PhoenixDataAccessException;
import com.volvo.phoenix.orion.util.OrionCMContext;

/**
 * Internal implementaion of move/copy operation.
 */
@Service
@Transactional
class DocumentOperationExecutorImpl {

    private static final String ORION_VAULT = "app_phoenix";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AuditLogTXHelper auditLogHelper;
    @Autowired
    private OperationService operationService;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private OrionDocumentRepository orionDocumentRepository;
    @Autowired
    private NodeResolver nodeResolver;
    @Autowired
    private SolutionParamRepository solutionParamRepository;
    @Autowired
    private CopyManagerTXExecutor copyManagerTXExecutor;
    @Autowired
    private DocumentStructureService documentStructureService;
    @Autowired
    private LdapUserService ldapUserService;
    @Autowired
    private UrlRequestQueryParamsServiceImpl urlRequestQueryParams;

    final JavaMailSenderImpl sender = new JavaMailSenderImpl();

    /**
     * Performs necessary logic for move/copy operation. Populates and stores audit trail (history of operation).
     * 
     * @param operation
     *            operation to be executed
     */
    public void executeOperation(Operation operation) {
        if (null != operation) {
            final TreeNodeDTO sourceNode = nodeResolver.resolveNode(operation.getSourceType(), operation.getSourceId());
            final TreeNodeDTO targetNode = nodeResolver.resolveFolderNode(operation.getTargetFolderId());
            OperationStatus operationStatus = OperationStatus.SUCCEEDED;
            try {
                logger.info("perform " + operation.getOperationType() + " " + sourceNode.getPath() + "->" + targetNode.getPath());
                final List<SolutionParam> solutions = solutionParamRepository.findByOperationId(operation.getId());

                if (isDocumentOperation(sourceNode)) {
                    executeDocumentOperation(operation, sourceNode, targetNode, solutions);
                } else {
                    executeFolderOperation(operation, sourceNode, targetNode, solutions);
                }
                operationService.closeOperation(operation.getId(), OperationStatus.SUCCEEDED);

                logger.info("finished successfully " + operation.getOperationType() + " " + sourceNode.getPath() + "->" + targetNode.getPath());

                if (operation.getOperationType() == OperationType.MOVE) {
                    switch (operation.getSourceType()) {
                    case FOLDER:
                        sendFolderNotificationToOwners(operation.getUser(), operation.getSourceId(), targetNode.getPath() + "\\" + sourceNode.getName());
                        break;
                    case DOCUMENT:
                        final String sourceFolder = sourceNode.getPath() != null && !sourceNode.getPath().isEmpty() ? sourceNode.getPath().substring(0, sourceNode.getPath().lastIndexOf("\\") + 1) : "";
                        final String targetFolder = targetNode.getPath() + "\\";
                        sendDocumentNotificationToOwners(operation.getUser(), operation.getSourceId(), sourceFolder.replaceFirst("\\\\", ""), targetFolder.replaceFirst("\\\\", ""));
                        break;
                    default:
                        logger.info("Unknown source type {}", operation.getSourceType());
                    }
                }
            } catch (Exception e) {
                operationService.closeOperation(operation.getId(), OperationStatus.FAILED);
                logger.error("Operation ID =" + operation.getId() + " failed", e);
                operationStatus = OperationStatus.FAILED;
            }
            finally{
                sendEmailToOperationOwner(operation, sourceNode, targetNode, operationStatus);
            }
        }
    }

    private void sendEmailToOperationOwner(final Operation operation, final TreeNodeDTO sourceNode, final TreeNodeDTO targetNode, final OperationStatus operationStatus) {
        logger.info("Preparing to send email notification to the operation owner.");

        final List<String> ownerEmails = ldapUserService.getUserEmails(operation.getUser());
        if (ownerEmails != null && !ownerEmails.isEmpty()) {
            final String emailSubject = String.format("[Copy Manager] Your planned operation has finished with a status: %s", operationStatus);
            final String operationDetails = String.format("From: %s\nTo: %s\nType: %s", sourceNode.getPath(), targetNode.getPath(), operation.getOperationType());
            final String emailContent = String.format("The operation you requested has been finished with the status %s.\n\nOperation details:\n%s\n\n"
                                                              + "In case of any questions related to the operation please use the following reference number: %d\n\nBest regards,\nPhoenix application",
                                                      operationStatus, operationDetails, operation.getId());
            sendOwnerEmail(ownerEmails.get(0), emailSubject, emailContent);
        } else {
            logger.info("The operation owner {} has no email address. The notification was not send", operation.getUser());
        }
    }

    private void sendDocumentNotificationToOwners(final String operatorId, final long documentId, final String sourceFolder, final String targetFolder) {
        logger.info("Preparing to send document email notification.");

        final Document document = documentRepository.findOne(documentId);
        final OrionDocument orionDocument = orionDocumentRepository.findOne(documentId);

        final Set<String> recipients = new LinkedHashSet<String>();

        // It prevents sending emails to the document/folcer owners. Instead the email is delivered to the operation owner (ie.: business admin)
        if (urlRequestQueryParams.getOwnerEmailNotificationsState() == NotificationState.DISABLED) {
            recipients.add(operatorId);
        } else {
            if (document.getAuthorId() != null && !document.getAuthorId().isEmpty()) {
                recipients.add(document.getAuthorId().toUpperCase());
            }
            if (document.getIssuerId() != null && !document.getIssuerId().isEmpty()) {
                recipients.add(document.getIssuerId().toUpperCase());
            }
        }
        
        for (final String recipient : recipients) {
            final List<String> emails = ldapUserService.getUserEmails(recipient);
            if (emails != null && !emails.isEmpty()) {
                String documentRegistrationNumber = orionDocument.getName();
                sendOwnerEmail(emails.get(0), "[Phoenix] Your Phoenix document has been moved.",
                               composeMailBodyContentForDocument(operatorId, documentRegistrationNumber, document.getTitle(), sourceFolder, targetFolder));
            }
        }
    }

    private String composeMailBodyContentForDocument(final String operatorId, final String documentRegistrationNumber, final String documentTitle,
            final String sourcePath, final String targetPath) {

        final StringBuilder messageContent = new StringBuilder();
        messageContent.append("You are the Author/Issuer of Phoenix document which has been moved.\n\n").append("Document title: ").append(documentTitle)
                      .append("\n").append("Registration number: ").append(documentRegistrationNumber).append("\nFrom: ").append(sourcePath).append("\nTo: ")
                      .append(targetPath).append("\n\n").append("The change was done by Business Administrator ").append(operatorId).append("\n")
                      .append("Any external links will not change.\n\n").append("Best regards,\n").append("Phoenix application");

        return messageContent.toString();
    }

    private void sendFolderNotificationToOwners(final String operatorId, final long nodeId, final String targetFolder) {

		logger.info("Preparing to send email notification.");
        
        final Map<String, List<String>> owners = composePathsForOwners(nodeId, targetFolder);
        final Set<String> ownerSet = owners.keySet();
        final boolean ownersNotificationEnabled = urlRequestQueryParams.getOwnerEmailNotificationsState() != NotificationState.DISABLED;
        final List<String> operationOwnerEmails = ownersNotificationEnabled ? null : ldapUserService.getUserEmails(operatorId);
        
        // It prevents sending emails to the document/folcer owners. Instead the email is delivered to the operation owner (ie.: business admin)
        for (final String owner : ownerSet) {
            final List<String> emails = ownersNotificationEnabled ? ldapUserService.getUserEmails(owner) : operationOwnerEmails;
            if (emails != null && !emails.isEmpty()) {
                final String ownerEmail = composeMailBodyContent(operatorId, nodeId, owners.get(owner));
                sendOwnerEmail(emails.get(0), "[Phoenix] Your Phoenix folders have been moved", ownerEmail);
            }
        }
    }

    private void sendOwnerEmail(final String ownerEmail, final String mailSubject, final String ownerEmailContent) {

        sender.setHost("mailgot.it.volvo.net");

        final MimeMessage message = sender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setSubject(mailSubject);
            helper.setFrom("phoenix@volvo.com");
            helper.setTo(ownerEmail);
            helper.setBcc("daniel.marzec@consultant.volvo.com");
            helper.setText(ownerEmailContent);
            sender.send(message);
            logger.info("Notification has been sent.");
        } catch (MessagingException e) {
            logger.info("Notification could not be sent.", e);
        }
    }

    private Map<String, List<String>> composePathsForOwners(final long nodeId, final String targetFolder) {
        logger.info("Composing owners path after 'move' operation.");

        final Map<String, List<String>> owners = new HashMap<String, List<String>>();
        final TreeNodeDTO sourceNode = nodeResolver.resolveNode(ItemType.FOLDER, nodeId);
        final List<String> newOwnerPaths = new ArrayList<String>();
        newOwnerPaths.add("From: " + sourceNode.getPath() + "\nTo: " + targetFolder + "\n");
        owners.put(sourceNode.getOwner(), newOwnerPaths);

        final TreeNodeDTO sourceFolderTree = documentStructureService.folderTree(nodeId);
        final List<TreeNodeDTO> nodes = sourceFolderTree.getChildren();

        for (TreeNodeDTO treeNodeDTO : nodes) {
            final Map<String, List<String>> ow = composePathsForOwners(treeNodeDTO.getId(), targetFolder + "\\" + treeNodeDTO.getName());
            final Set<String> keys = ow.keySet();
            for (String key : keys) {
                if (owners.containsKey(key)) {
                    final List<String> paths = owners.get(key);
                    paths.addAll(ow.get(key));
                } else {
                    owners.putAll(ow);
                }
            }
        }
        return owners;
    }

    private String composeMailBodyContent(final String operatorId, final long operation, final List<String> paths) {
        final StringBuilder messageContent = new StringBuilder();
        messageContent.append("You are the owner of Phoenix folder which has been moved.\n\n");

        for (String path : paths) {
            final String folderName = path.substring(path.lastIndexOf("\\") + 1).trim();
            messageContent.append("Folder: ").append(folderName).append("\n").append(path + "\n");
        }
        messageContent.append("The change was done by Business Administrator ").append(operatorId).append("\n")
                      .append("Any external links will not change.\n\n").append("Best regards,\n").append("Phoenix application");

        return messageContent.toString();
    }

    private void executeFolderOperation(Operation operation, TreeNodeDTO sourceNode, TreeNodeDTO targetNode, List<SolutionParam> solutions)
            throws PhoenixDataAccessException, IOException {
        FolderAuditLogDTO folderAuditLog = auditLogHelper.populateSourceFolderAuditLog(operation, sourceNode, targetNode);
        folderAuditLog = auditLogHelper.populateWhoAndWhenAuditLog(operation, folderAuditLog);

        if (operation.getOperationType() == OperationType.COPY) {
            OrionCMContext orionContext = new OrionCMContext(operation.getUser(), ORION_VAULT);
            copyManagerTXExecutor.copyFolderTX(orionContext, operation.getSourceId(), operation.getTargetFolderId(), solutions);
        } else if (operation.getOperationType() == OperationType.MOVE) {
            copyManagerTXExecutor.moveFolderTX(operation.getSourceId(), operation.getTargetFolderId(), solutions);
        } else {
            logger.warn("Invalid operation {}", operation.getOperationType());
            return;
        }
        auditLogHelper.storeAuditTrail(folderAuditLog);
    }

    private void executeDocumentOperation(Operation operation, TreeNodeDTO sourceNode, TreeNodeDTO targetNode, List<SolutionParam> solutions)
            throws PhoenixDataAccessException, IOException {
        DocumentAuditLogDTO docAuditLog = auditLogHelper.populateSourceDocAuditLog(operation, sourceNode, targetNode);
        docAuditLog = auditLogHelper.populateWhoAndWhenAuditLog(operation, docAuditLog);
        Long targetDocumentId = null;
        if (operation.getOperationType() == OperationType.COPY) {
            OrionCMContext orionContext = new OrionCMContext(operation.getUser(), ORION_VAULT);
            targetDocumentId = copyManagerTXExecutor.copyDocumentTX(orionContext, operation.getSourceId(), operation.getTargetFolderId(), solutions);
        } else if (operation.getOperationType() == OperationType.MOVE) {
            copyManagerTXExecutor.moveDocumentTX(operation.getSourceId(), operation.getTargetFolderId(), solutions);
            targetDocumentId = sourceNode.getId();
        } else {
            logger.warn("Invalid operation {}", operation.getOperationType());
            return;
        }

        docAuditLog = auditLogHelper.populateTargetDocAuditLog(operation, targetDocumentId, docAuditLog);
        auditLogHelper.storeAuditTrail(docAuditLog);
    }

 

    private boolean isDocumentOperation(TreeNodeDTO node) {
        return NodeType.D == node.getType();
    }

 
}
