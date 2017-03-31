package com.volvo.phoenix.document.service;

import com.volvo.phoenix.document.dto.ConflictDTO;
import com.volvo.phoenix.document.dto.OperationDTO;

/**
 * Represents rule that checks whether any conflict exists for copied/moved document/folder.
 */
public interface ConflictRule {

    /**
     * Verifies whether given operation breaks rules and make conflict as described in requirements.
     * 
     * @param operation
     * @return conflict if any rule is broken or null if evberything is according he rules
     */
    ConflictDTO check(OperationDTO operation);
    
    byte getOrderInConflictRuleChain();

}

