package com.volvo.phoenix.document.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.volvo.phoenix.document.datatype.ConflictType;
import com.volvo.phoenix.document.entity.SolutionParam;

public interface SolutionParamRepository extends CrudRepository<SolutionParam, Long> {

    List<SolutionParam> findByOperationIdAndSolution(Long id, ConflictType type);
    
    List<SolutionParam> findByOperationId(Long operationId);

}
