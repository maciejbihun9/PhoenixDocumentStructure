package com.volvo.phoenix.document.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.volvo.phoenix.document.entity.FolderAuditLog;


/**
 * Repository for {@link FolderAuditLog}.
 *
 * @author bpld313
 *
 */
public interface FolderAuditLogRepository extends PagingAndSortingRepository<FolderAuditLog, Long>  {
    Page<FolderAuditLog> findByUserVcnIdOrderByWhenDesc(String userVcnId, Pageable pageable);
}
