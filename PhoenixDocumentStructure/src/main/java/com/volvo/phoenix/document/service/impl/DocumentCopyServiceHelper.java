package com.volvo.phoenix.document.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.volvo.phoenix.document.datatype.ConflictType;
import com.volvo.phoenix.document.dto.ConflictDTO;
import com.volvo.phoenix.document.dto.OperationDTO;
import com.volvo.phoenix.document.repository.DocumentRepository;
import com.volvo.phoenix.document.repository.FolderRepository;
import com.volvo.phoenix.document.repository.SolutionParamRepository;
import com.volvo.phoenix.document.service.ConflictRule;
import com.volvo.phoenix.document.translator.DocumentTranslator;
import com.volvo.phoenix.document.translator.FolderTranslator;
import com.volvo.phoenix.document.translator.SolutionParamTranslator;

@Service
@Transactional
public class DocumentCopyServiceHelper {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentTranslator documentTranslator;

    @Autowired
    private FolderTranslator folderTranslator;

    @Autowired
    private List<ConflictRule> rules;

    @Autowired
    private SolutionParamRepository solutionParamRepository;

    @Autowired
    private SolutionParamTranslator solutionParamTranslator;

    @PostConstruct
    private void orderConflictRules() {
        Collections.sort(rules, new ConflictRuleComparator());
    }
    
    public List<ConflictDTO> checkConflicts(final OperationDTO operationDTO) {

        final List<ConflictDTO> conflicts = new ArrayList<ConflictDTO>();

        if (CollectionUtils.isNotEmpty(rules)) {
            for (final ConflictRule rule : rules) {

                final ConflictDTO conflictDTO = rule.check(operationDTO);
                if (conflictDTO != null) {
                    conflicts.add(conflictDTO);
                    if (conflictDTO.getType() == ConflictType.APP || conflictDTO.getType() == ConflictType.ROOT) {
                        break;
                    }
                }
                // if (conflictDTO != null) {
                // conflictDTO.setOperation(operationDTO);
                // // load solutions for the conflicts
                // List<SolutionParam> params = solutionParamRepository.findById_OperationIdAndId_Solution(operationDTO.getId(), conflictDTO.getType());
                // conflictDTO.setSolutionParams(solutionParamTranslator.translateToDto(params));
                // rule.checkIfProposedSolutionsSolveConflict(conflictDTO);
                // conflicts.add(conflictDTO);
                // }
            }
        }
        return conflicts;
    }

    /**
     * Checks if all conflicts have proposed solution that fully solve each conflict.
     *
     * @param conflicts
     * @return true if all conflicts have proposed solutions that solves them all, otherwise false
     */
    public boolean allConflictsHaveProposedSolutions(List<ConflictDTO> conflicts) {
        throw new UnsupportedOperationException();
        // if (CollectionUtils.isEmpty(conflicts)) {
        // return true;
        // }
        //
        // for (ConflictDTO conflict : conflicts) {
        // if (!conflict.isProposedSolutionSolvesConflict()) {
        // return false;
        // }
        // }
        //
        // return true;
    }

    /**
     * Solves all conflicts.
     *
     * @param operation
     * @param conflicts
     */
    public void solveConflicts(OperationDTO operation, List<ConflictDTO> conflicts) {
        throw new UnsupportedOperationException();
        // if (CollectionUtils.isNotEmpty(rules)) {
        // for (ConflictRule rule : rules) {
        // for (ConflictDTO conflict : conflicts) {
        // if (rule.accept(conflict)) {
        // rule.resolve(conflict);
        // }
        // }
        // }
        // }
    }
    
    class ConflictRuleComparator implements Comparator<ConflictRule> {

        @Override
        public int compare(final ConflictRule o1, final ConflictRule o2) {
            return o1.getOrderInConflictRuleChain() - o2.getOrderInConflictRuleChain();
        }
        
    }
}
