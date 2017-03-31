package com.volvo.phoenix.document.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.volvo.phoenix.document.datatype.ConflictType;
import com.volvo.phoenix.document.datatype.NodeType;
import com.volvo.phoenix.document.dto.ConflictDTO;
import com.volvo.phoenix.document.dto.FolderCannotMoveConflictDTO;
import com.volvo.phoenix.document.dto.FolderDTO;
import com.volvo.phoenix.document.dto.OperationDTO;
import com.volvo.phoenix.document.entity.Folder;
import com.volvo.phoenix.document.repository.FolderRepository;
import com.volvo.phoenix.document.service.ConflictRule;

@Component
@Transactional(propagation = Propagation.SUPPORTS)
public class FolderCannotBeMovedRule implements ConflictRule {

    private final byte ruleOrder = 1;
    
    @Autowired
    private FolderRepository folderRepository;


    @Override
    public ConflictDTO check(final OperationDTO operationDTO) {

        Assert.notNull(operationDTO, "The 'operationDTO' parameter cannot be null");
        Assert.notNull(operationDTO.getSource());

        if (operationDTO.getSource().getType() != NodeType.D) {

            final Folder sourceFolder = folderRepository.findOne(Long.valueOf(operationDTO.getSource().getId()));
            return sourceFolder != null && isFolderConnectedWithProjectOrApplication(sourceFolder) ? new FolderCannotMoveConflictDTO(new FolderDTO()) : null;
        }
        return null;
    }

    private boolean isFolderConnectedWithProjectOrApplication(final Folder folder) {

        if (folder.getAcl().getProjectNumber() != null) {
            return true;
        }

        final List<Folder> subFolders = folderRepository.findByParent_Id(folder.getId());
        for (final Folder subFolder : subFolders) {
            if (subFolder.getAcl().getProjectNumber() != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public byte getOrderInConflictRuleChain() {
        return ruleOrder;
    }

}
