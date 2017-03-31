package com.volvo.phoenix.document.service.impl;

import com.volvo.phoenix.document.entity.Operation;

public interface OperationFactory {

    Operation createOperation(long sourceId, long targetId, String user);

}
