package com.volvo.phoenix.document.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.volvo.phoenix.document.entity.TransactionStack;
import com.volvo.phoenix.document.entity.TransactionStackPK;

/**
 * Repository for {@link TransactionStack}.
 * 
 */
public interface TransactionStackRepository extends CrudRepository<TransactionStack, TransactionStackPK> {    
    
    List<TransactionStack> findById_DocumentId(Long documentId);

    @Query(value="SELECT MAX(REV) as REV FROM V0PHOENIX.VT_PHX_DOC_LTST_VALID_OR_WORK WHERE registration_number = :registrationNumber", nativeQuery=true)
    String getLatestRevisionValidOrInWorkNotProtected(@Param("registrationNumber") String registrationNumber);
}
