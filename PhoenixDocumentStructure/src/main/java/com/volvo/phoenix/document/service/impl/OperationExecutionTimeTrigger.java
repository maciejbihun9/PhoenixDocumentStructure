package com.volvo.phoenix.document.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.volvo.phoenix.document.entity.Operation;
import com.volvo.phoenix.document.service.OperationService;

/**
 * Time based scheduler triggers ready operation to be executed each predefined time period ({@link #executeScheduledOperation()}. It delegates execution to
 * {@link DocumentOperationExecutorImpl}.
 */
@Service
public class OperationExecutionTimeTrigger {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DocumentOperationExecutorImpl documentOperationExecutor;
    @Autowired
    private OperationService operationService;

    /**
     * Finds scheduled operation ready to run and execute it.
     */
    @Scheduled(cron = "*/30 * * * * ?")
    @Transactional
    public void executeScheduledOperation() {
        logger.info("Timer triggers operation execution job.");
        try {
            for (Operation operation : operationService.getOperationsToRun()) {
                 documentOperationExecutor.executeOperation(operation); 
            }
        } catch (Exception e) {
            logger.info("Operation doesn't exist. Logic error or someone remove the record from the database? ", e);
        }
    }

    /**
     * Finds user operations with status CREATE older that the set period of time and delete it.
     */
    @Scheduled(fixedDelay = 300000)
    @Transactional
    public void deleteDeprecatedUserOperations() {
        logger.info("Timer trigger looking for expired user operations that will be removed.");
        operationService.removeExpiredUserOperations();
    }

}
