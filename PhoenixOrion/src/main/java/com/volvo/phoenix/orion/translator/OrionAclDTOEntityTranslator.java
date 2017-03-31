package com.volvo.phoenix.orion.translator;

import com.volvo.phoenix.orion.dto.OrionAclDTO;
import com.volvo.phoenix.orion.entity.OrionAcl;

/**
 * Translator to translate DTO and entity
 * 
 * @author v0cn181
 */
public class OrionAclDTOEntityTranslator {

    public static com.volvo.phoenix.orion.dto.OrionAclDTO toDTO(OrionAcl save) {

        OrionAclDTO acl = new com.volvo.phoenix.orion.dto.OrionAclDTO();
        acl.setId(save.getId());
        acl.setName(save.getName());
        return acl;
    }

}
