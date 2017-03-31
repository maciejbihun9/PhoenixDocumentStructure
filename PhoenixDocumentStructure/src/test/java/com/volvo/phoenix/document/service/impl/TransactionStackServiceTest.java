package com.volvo.phoenix.document.service.impl;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.volvo.phoenix.document.datatype.TransactionStackEventType;
import com.volvo.phoenix.document.datatype.TransactionStackSystemType;
import com.volvo.phoenix.document.entity.Document;
import com.volvo.phoenix.document.entity.DocumentStatus;
import com.volvo.phoenix.document.entity.TransactionStack;
import com.volvo.phoenix.document.repository.DocumentRepository;
import com.volvo.phoenix.document.repository.TransactionStackRepository;
import com.volvo.phoenix.orion.dto.OrionAclStateDTO;
import com.volvo.phoenix.orion.dto.OrionDocumentDTO;
import com.volvo.phoenix.orion.service.OrionDocumentService;

/**
 * Tests the indexing events service (aka Transaction Stack) in Copy Manager solution
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class TransactionStackServiceTest {

    @Mock
    private OrionDocumentService orionDocumentService;
    
    @Mock
    private TransactionStackRepository transactionStackRepository;
    
    @Mock
    private DocumentRepository documentRepository;
    
    @InjectMocks
    private TransactionStackServiceImpl transactionStackService;

    @Captor
    private ArgumentCaptor<TransactionStack> captor;

    @Test
    public void shouldStoreSolrAndESEAddEventWhenDocumentCopied() {
        // given a valid document ,latest version, info class = INTERNAL
        Document sourceDocument = prepareValidDocument(-1L);
        OrionDocumentDTO orionDocument = prepareValidOrionDocumentDTO(-1L, "2");
        
        Mockito.when(orionDocumentService.findDocument(Mockito.anyLong())).thenReturn(orionDocument);
        Mockito.when(documentRepository.findOne(Mockito.anyLong())).thenReturn(sourceDocument);
        Mockito.when(transactionStackRepository.getLatestRevisionValidOrInWorkNotProtected(Mockito.anyString())).thenReturn("2");
        Mockito.when(transactionStackRepository.save(captor.capture())).thenReturn(null);
        
        // when document is copied to new location
        transactionStackService.documentCopied(Long.valueOf(-1));

        // then there should be 1 ADD transaction to ESE and 1 ADD transaction to Solr triggered
        Assertions.assertThat(captor.getAllValues().size()).isEqualTo(2);
        Assertions.assertThat(captor.getAllValues().get(0).getDocumentId()).isEqualTo(Long.valueOf(-1));
        Assertions.assertThat(captor.getAllValues().get(0).getTransactionType()).isEqualTo(TransactionStackEventType.A);
        Assertions.assertThat(captor.getAllValues().get(0).getSystemType()).isEqualTo(TransactionStackSystemType.E);
        
        Assertions.assertThat(captor.getAllValues().get(1).getDocumentId()).isEqualTo(Long.valueOf(-1));
        Assertions.assertThat(captor.getAllValues().get(1).getTransactionType()).isEqualTo(TransactionStackEventType.A);
        Assertions.assertThat(captor.getAllValues().get(1).getSystemType()).isEqualTo(TransactionStackSystemType.S);
    }
    
    @Test
    public void shouldStoreSolrAddEventOnlyWhenStrictlyConfidentialDocumentCopied() {
        // given a valid document ,latest version, info class = STRICTLY CONFIDENTIAL
        Document sourceDocument = prepareValidDocument(-1L);
        OrionDocumentDTO orionDocument = prepareValidOrionDocumentDTO(-1L, "2");
        OrionAclStateDTO aclState = new OrionAclStateDTO();
        aclState.setId(40L); //STRICTLY CONFIDENTIAL
        orionDocument.setAclState(aclState);
        
        Mockito.when(orionDocumentService.findDocument(Mockito.anyLong())).thenReturn(orionDocument);
        Mockito.when(documentRepository.findOne(Mockito.anyLong())).thenReturn(sourceDocument);
        Mockito.when(transactionStackRepository.getLatestRevisionValidOrInWorkNotProtected(Mockito.anyString())).thenReturn("2");
        Mockito.when(transactionStackRepository.save(captor.capture())).thenReturn(null);

        // when document is copied to new location
        transactionStackService.documentCopied(Long.valueOf(-1));

        // then there should be 1 ADD transaction to Solr triggered
        Assertions.assertThat(captor.getAllValues().size()).isEqualTo(1);
        Assertions.assertThat(captor.getAllValues().get(0).getDocumentId()).isEqualTo(Long.valueOf(-1));
        Assertions.assertThat(captor.getAllValues().get(0).getTransactionType()).isEqualTo(TransactionStackEventType.A);
        Assertions.assertThat(captor.getAllValues().get(0).getSystemType()).isEqualTo(TransactionStackSystemType.S);
    }
    
    @Test
    public void shouldNotStoreAnyEventWhenProtectInWorkDocumentCopied() {
        // given document in the latest version and status In-Work but Protected In-Work
        Document sourceDocument = prepareValidDocument(-1L);
        sourceDocument.setProtectInWork("Y");
        DocumentStatus ds = new DocumentStatus();
        ds.setStatus("WORK");
        sourceDocument.setStatus(ds);
        
        OrionDocumentDTO orionDocument = prepareValidOrionDocumentDTO(-1L, "2");
        
        Mockito.when(orionDocumentService.findDocument(Mockito.anyLong())).thenReturn(orionDocument);
        Mockito.when(documentRepository.findOne(Mockito.anyLong())).thenReturn(sourceDocument);
        Mockito.when(transactionStackRepository.getLatestRevisionValidOrInWorkNotProtected(Mockito.anyString())).thenReturn("2");

        // when document is copied to new location
        transactionStackService.documentCopied(Long.valueOf(-1));

        // then there should be no transactions for triggering indexing
        Mockito.verify(transactionStackRepository, Mockito.times(0)).save(Mockito.any(TransactionStack.class));
    }
    
    @Test
    public void shouldStoreSolrAndESEUpdateEventWhenDocumentMoved() {
        // given a valid document ,latest version, info class = INTERNAL
        Document sourceDocument = prepareValidDocument(-1L);
        OrionDocumentDTO orionDocument = prepareValidOrionDocumentDTO(-1L, "2");
        OrionDocumentDTO previousDocument = prepareValidOrionDocumentDTO(-2L, "1");
        
        Mockito.when(orionDocumentService.findDocument(Long.valueOf(-1L))).thenReturn(orionDocument);
        Mockito.when(orionDocumentService.findDocument(Long.valueOf(-2L))).thenReturn(previousDocument);
        Mockito.when(documentRepository.findOne(Mockito.anyLong())).thenReturn(sourceDocument);
        Mockito.when(transactionStackRepository.getLatestRevisionValidOrInWorkNotProtected(Mockito.anyString())).thenReturn("2");
        Mockito.when(transactionStackRepository.save(captor.capture())).thenReturn(null);

        // when all document revisions is moved to new location
        transactionStackService.documentMoved(Long.valueOf(-1));
        transactionStackService.documentMoved(Long.valueOf(-2));

        // then there should be 1 UPDATE transaction to ESE and 1 UPDATE transaction to Solr triggered
        Assertions.assertThat(captor.getAllValues().size()).isEqualTo(2);
        Assertions.assertThat(captor.getAllValues().get(0).getDocumentId()).isEqualTo(Long.valueOf(-1));
        Assertions.assertThat(captor.getAllValues().get(0).getTransactionType()).isEqualTo(TransactionStackEventType.U);
        Assertions.assertThat(captor.getAllValues().get(0).getSystemType()).isEqualTo(TransactionStackSystemType.E);
        
        Assertions.assertThat(captor.getAllValues().get(1).getDocumentId()).isEqualTo(Long.valueOf(-1));
        Assertions.assertThat(captor.getAllValues().get(1).getTransactionType()).isEqualTo(TransactionStackEventType.U);
        Assertions.assertThat(captor.getAllValues().get(1).getSystemType()).isEqualTo(TransactionStackSystemType.S);
    }
    
    @Test
    public void shouldStoreSolrUpdateEventOnlyWhenStrictlyConfidentialDocumentMoved() {
        // given a valid document ,latest version, info class = STRICTLY CONFIDENTIAL
        Document sourceDocument = prepareValidDocument(-1L);
        OrionDocumentDTO orionDocument = prepareValidOrionDocumentDTO(-1L, "2");
        OrionAclStateDTO aclState = new OrionAclStateDTO();
        aclState.setId(40L); //STRICTLY CONFIDENTIAL
        orionDocument.setAclState(aclState);
        
        Mockito.when(orionDocumentService.findDocument(Mockito.anyLong())).thenReturn(orionDocument);
        Mockito.when(documentRepository.findOne(Mockito.anyLong())).thenReturn(sourceDocument);
        Mockito.when(transactionStackRepository.getLatestRevisionValidOrInWorkNotProtected(Mockito.anyString())).thenReturn("2");
        Mockito.when(transactionStackRepository.save(captor.capture())).thenReturn(null);

        // when document is moved to new location
        transactionStackService.documentMoved(Long.valueOf(-1));

        // then there should be 1 UPDATE transaction to Solr triggered
        Assertions.assertThat(captor.getAllValues().size()).isEqualTo(1);
        Assertions.assertThat(captor.getAllValues().get(0).getDocumentId()).isEqualTo(Long.valueOf(-1));
        Assertions.assertThat(captor.getAllValues().get(0).getTransactionType()).isEqualTo(TransactionStackEventType.U);
        Assertions.assertThat(captor.getAllValues().get(0).getSystemType()).isEqualTo(TransactionStackSystemType.S);
    }
    
    @Test
    public void shouldNotStoreAnyEventWhenNotTheLatestDocumentMoved() {
        // given document in the older version than the latest
        Document sourceDocument = prepareValidDocument(-1L);
        OrionDocumentDTO orionDocument = prepareValidOrionDocumentDTO(-1L, "2");
        orionDocument.setRevision("1");
        
        Mockito.when(orionDocumentService.findDocument(Mockito.anyLong())).thenReturn(orionDocument);
        Mockito.when(documentRepository.findOne(Mockito.anyLong())).thenReturn(sourceDocument);
        Mockito.when(transactionStackRepository.getLatestRevisionValidOrInWorkNotProtected(Mockito.anyString())).thenReturn("2");

        // when document is moved to new location
        transactionStackService.documentCopied(Long.valueOf(-1));

        // then there should be no transactions for triggering indexing
        Mockito.verify(transactionStackRepository, Mockito.times(0)).save(Mockito.any(TransactionStack.class));
    }
    
    @Test
    public void shouldStoreSolrAndESEAddEventAndSolrAndESEDeleteEventWhenNewVersionCreated() {
        // given a valid document ,latest version, info class = INTERNAL
        Document sourceDocument = prepareValidDocument(-1L);
        OrionDocumentDTO orionDocument = prepareValidOrionDocumentDTO(-1L, "2");
        OrionDocumentDTO previousDocument = prepareValidOrionDocumentDTO(-2L, "1");
        
        Mockito.when(orionDocumentService.findDocument(Mockito.anyLong())).thenReturn(orionDocument);
        Mockito.when(documentRepository.findOne(Mockito.anyLong())).thenReturn(sourceDocument);
        Mockito.when(orionDocumentService.findDocument(Long.valueOf(-2L))).thenReturn(previousDocument);
        Mockito.when(transactionStackRepository.getLatestRevisionValidOrInWorkNotProtected(Mockito.anyString())).thenReturn("2");
        Mockito.when(transactionStackRepository.save(captor.capture())).thenReturn(null);
        
        // when document is copied to new location
        transactionStackService.newVersionCreated(-1L, -2L);

        // then there should be 1 ADD transaction to ESE and 1 ADD transaction to Solr triggered for new version of the document
        Assertions.assertThat(captor.getAllValues().size()).isEqualTo(4);
        Assertions.assertThat(captor.getAllValues().get(0).getDocumentId()).isEqualTo(Long.valueOf(-1));
        Assertions.assertThat(captor.getAllValues().get(0).getTransactionType()).isEqualTo(TransactionStackEventType.A);
        Assertions.assertThat(captor.getAllValues().get(0).getSystemType()).isEqualTo(TransactionStackSystemType.E);
        
        Assertions.assertThat(captor.getAllValues().get(1).getDocumentId()).isEqualTo(Long.valueOf(-1));
        Assertions.assertThat(captor.getAllValues().get(1).getTransactionType()).isEqualTo(TransactionStackEventType.A);
        Assertions.assertThat(captor.getAllValues().get(1).getSystemType()).isEqualTo(TransactionStackSystemType.S);
        
        // and there should be 1 DELETE transaction to ESE and 1 DELETE transaction to Solr triggered for new previous of the document
        Assertions.assertThat(captor.getAllValues().get(2).getDocumentId()).isEqualTo(Long.valueOf(-2));
        Assertions.assertThat(captor.getAllValues().get(2).getTransactionType()).isEqualTo(TransactionStackEventType.D);
        Assertions.assertThat(captor.getAllValues().get(2).getSystemType()).isEqualTo(TransactionStackSystemType.E);
        
        Assertions.assertThat(captor.getAllValues().get(3).getDocumentId()).isEqualTo(Long.valueOf(-2));
        Assertions.assertThat(captor.getAllValues().get(3).getTransactionType()).isEqualTo(TransactionStackEventType.D);
        Assertions.assertThat(captor.getAllValues().get(3).getSystemType()).isEqualTo(TransactionStackSystemType.S);
    }

    private OrionDocumentDTO prepareValidOrionDocumentDTO(Long id, String revision) {
        OrionDocumentDTO d = new OrionDocumentDTO();
        d.setId(id);
        d.setName("12345");
        d.setRevision(revision);
        
        OrionAclStateDTO aclState = new OrionAclStateDTO();
        aclState.setId(42L); //Internal
        d.setAclState(aclState);
        return d;
    }

    private Document prepareValidDocument(Long id) {
        Document d = new Document();
        d.setId(id);
        d.setProtectInWork("N");
        
        DocumentStatus ds = new DocumentStatus();
        ds.setStatus("VALID");
        d.setStatus(ds);
        return d;
    }

}
