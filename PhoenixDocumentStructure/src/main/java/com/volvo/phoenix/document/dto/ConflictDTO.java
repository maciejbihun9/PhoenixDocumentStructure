package com.volvo.phoenix.document.dto;

import java.io.Serializable;
import java.util.List;

import com.volvo.phoenix.document.datatype.ConflictLevel;
import com.volvo.phoenix.document.datatype.ConflictType;

/**
 * Base class of different TYPE of conflicts.
 * 
 * @author v0cn181
 */
public abstract class ConflictDTO implements Serializable {

    private static final long serialVersionUID = -5311881366785757593L;

    /**
     * The operation, who own this conflict.
     */
    private OperationDTO operation;

    /**
     * Parameters used to describe the solution of the conflict.
     */
    private List<SolutionParamDTO> solutionParams;

    /**
     * Whether proposed solution is enough to solve the conflict.
     */
    private boolean proposedSolutionSolvesConflict;

    public boolean isProposedSolutionSolvesConflict() {
        return proposedSolutionSolvesConflict;
    }

    public void setProposedSolutionSolvesConflict(boolean resolved) {
        this.proposedSolutionSolvesConflict = resolved;
    }

    public abstract ConflictType getType();

    public abstract ConflictLevel getLevel();

    public List<SolutionParamDTO> getSolutionParams() {
        return solutionParams;
    }

    public void setSolutionParams(List<SolutionParamDTO> params) {
        this.solutionParams = params;
    }

    public OperationDTO getOperation() {
        return operation;
    }

    public void setOperation(OperationDTO op) {
        this.operation = op;
    }

}
