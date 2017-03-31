package com.volvo.phoenix.document.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.volvo.phoenix.document.entity.DocumentAuditLog;


/**
 * Repository for {@link DocumentAuditLog}.
 *
 * @author bpld313
 *
 */
public interface DocumentAuditLogRepository extends PagingAndSortingRepository<DocumentAuditLog, Long>  {
    Page<DocumentAuditLog> findByUserVcnIdOrderByWhenDesc(String userVcnId, Pageable pageable);
}
