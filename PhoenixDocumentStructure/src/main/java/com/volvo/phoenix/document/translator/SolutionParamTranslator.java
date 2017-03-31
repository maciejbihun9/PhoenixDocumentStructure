package com.volvo.phoenix.document.translator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.volvo.phoenix.document.dto.SolutionParamDTO;
import com.volvo.phoenix.document.entity.SolutionParam;

@Component
@Transactional(propagation = Propagation.SUPPORTS)
public class SolutionParamTranslator {

    public List<SolutionParamDTO> translateToDto(List<SolutionParam> entities) {

        List<SolutionParamDTO> dtos = new ArrayList<SolutionParamDTO>();

        for (SolutionParam param : entities) {
            dtos.add(translateToDto(param));
        }

        return dtos;
    }

    private SolutionParamDTO translateToDto(SolutionParam entity) {
        SolutionParamDTO dto = new SolutionParamDTO();
        dto.setOperationId(entity.getOperationId());
        dto.setSolution(entity.getSolution());
        dto.setId(entity.getParamId());
        dto.setValue(entity.getValue());
        return dto;
    }

    public List<SolutionParam> translateToEntity(List<SolutionParamDTO> params) {
        List<SolutionParam> entities = Lists.newArrayList();
        for (SolutionParamDTO param : params) {
            entities.add(translateToEntity(param));
        }
        return entities;
    }

    private SolutionParam translateToEntity(SolutionParamDTO param) {
        SolutionParam entity = new SolutionParam();
        entity.setOperationId(param.getOperationId());
        entity.setSolution(param.getSolution());
        entity.setParamId(param.getId());
        entity.setValue(param.getValue());
        return entity;
    }

}
