package com.volvo.phoenix.orion.translator;

import com.volvo.phoenix.orion.dto.OrionAclStateDTO;

/**
 * Translator to translate DTO and entity
 * 
 * @author v0cn181
 */
public class AclStateDTOEntityTranslator {

    public static OrionAclStateDTO toDTO(com.volvo.phoenix.orion.entity.OrionAclState aclState) {
        if (aclState != null) {
            OrionAclStateDTO state = new OrionAclStateDTO();
            state.setId(aclState.getId());
            state.setName(aclState.getName());
            return state;
        }    
        return null;
    }

}
