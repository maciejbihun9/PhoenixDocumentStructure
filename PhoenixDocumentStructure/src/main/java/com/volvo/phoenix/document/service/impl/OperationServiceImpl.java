package com.volvo.phoenix.document.service.impl;

import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.volvo.phoenix.document.datatype.OperationStatus;
import com.volvo.phoenix.document.entity.Operation;
import com.volvo.phoenix.document.repository.OperationRepository;
import com.volvo.phoenix.document.service.OperationService;

@Service
@Transactional
public class OperationServiceImpl implements OperationService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private final Long MAX_OPERATION_TO_RUN_COUNT = 10l;
    
    @Autowired
    private OperationRepository operationRepository;

    /**
     * Finds and remove expired user operations managable by Copy Manager. Expired operation means: operation that exist longer than a specified time, i.e.: 2
     * hours.
     */
    @Override
    public void removeExpiredUserOperations() {
        final int OPERATION_EXPIRED_AFTER_HOURS = 2;
        logger.info("Looking for expired operations planned by any user and remove them");
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) - OPERATION_EXPIRED_AFTER_HOURS);
        operationRepository.deleteByCreateDateLessThanAndStatus(calendar.getTime(), OperationStatus.CREATED);
    }

    /**
     * Finds scheduled, ready to be executed operation.
     * 
     * @return
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW)    
    public List<Operation> getOperationsToRun() {
        List<Operation> operations = operationRepository.findScheduledOperationsToRun(MAX_OPERATION_TO_RUN_COUNT);
        for (Operation operation : operations) {
            operation.setStatus(OperationStatus.RUNNING);
            operationRepository.save(operation);
        }        
        return operations;
    }

    /**
     * Set operation status to ongoing: OperatioStatus.RUNNING
     * 
     * @param operation
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void markOperationAsOngoing(final Operation operation) {
        operation.setStatus(OperationStatus.RUNNING);
        operationRepository.save(operation);
    }

    /**
     * Close operation and set status.
     * 
     * @param id
     * @param status
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void closeOperation(Long operationId, OperationStatus status) {
        Operation operation  = operationRepository.findOne(operationId);
        operation.setStatus(status);
        operationRepository.save(operation);
    }

}