package com.volvo.phoenix.document.service;

import java.util.List;

import com.volvo.phoenix.document.datatype.OperationStatus;
import com.volvo.phoenix.document.entity.Operation;


/**
 * Maintain scheduled operations.
 */
public interface OperationService {

    void removeExpiredUserOperations();

    List<Operation> getOperationsToRun();

    void markOperationAsOngoing(Operation operation);

    void closeOperation(Long operationId, OperationStatus status);

}
