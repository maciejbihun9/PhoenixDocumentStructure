package com.volvo.phoenix.document.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.volvo.phoenix.document.datatype.OperationStatus;
import com.volvo.phoenix.document.entity.Operation;

/**
 * Repository for {@link Operation}.
 * 
 */
public interface OperationRepository extends CrudRepository<Operation, Long> {

    List<Operation> findByStatusAndSourceId(OperationStatus status, Long sourceId);
    
    List<Operation> findByStatusInAndSourceId(Collection<OperationStatus> status, Long sourceId);
    
    List<Operation> findByStatusAndTargetFolderId(OperationStatus status, Long targetFolderId);
    
    List<Operation> findByStatusInAndTargetFolderId(Collection<OperationStatus> status, Long targetFolderId);

    List<Operation> findByStatusAndUserOrderById(OperationStatus status, String name);    

    List<Operation> findByStatusNotAndUserOrderByCreateDateDesc(OperationStatus operationStatus, String userVcnId);

    void deleteByCreateDateLessThanAndStatus(Date expiryDate, OperationStatus status);

    @Query(value = "SELECT * FROM ( SELECT * FROM CM_OPERATION WHERE STATUS = 'SCHEDULED' ORDER BY ID ) WHERE rownum < :maxCount", nativeQuery = true)
    @Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
    List<Operation> findScheduledOperationsToRun(@Param("maxCount") Long maxOperationCount);
}
