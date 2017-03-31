package com.volvo.phoenix.document.translator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.volvo.phoenix.document.dto.FolderDTO;
import com.volvo.phoenix.document.dto.TreeNodeDTO;
import com.volvo.phoenix.document.entity.Folder;

@Component
public class FolderTranslator {

    @Autowired
    private PhoenixAclTranslator phoenixAclTranslator;
    
    /*
     * This is new Folder dto method used for getting only much needed folder information.
     */
    public FolderDTO translateToDTO2(Folder entity){
        FolderDTO folderDto = new FolderDTO();
        folderDto.setPath(entity.getPath());
        folderDto.setId(entity.getId());
        return folderDto;
    }

    public FolderDTO translateToDto(Folder entity) {
        FolderDTO dto = new FolderDTO();

        dto.setAcl(phoenixAclTranslator.translateToDto(entity.getAcl()));
        dto.setId(entity.getId());
        dto.setOwnerRealname(entity.getOwnerRealname());
        if (null != entity.getParent()) {
            FolderDTO parentFolderDto = translateToDto(entity.getParent());
            dto.setParent(parentFolderDto);
        }
        dto.setText(entity.getText());
        dto.setType(entity.getType());

        return dto;
    }

    public List<TreeNodeDTO> translateToTreeNodeDto(List<Folder> folders) {
        List<TreeNodeDTO> nodes = new ArrayList<TreeNodeDTO>();
        for (Folder folder : folders) {
            nodes.add(translateToTreeNodeDto(folder));
        }
        return nodes;
    }

    public TreeNodeDTO translateToTreeNodeDto(Folder folder) {
        TreeNodeDTO node = new TreeNodeDTO();
        node.setId(folder.getId());
        node.setParentId(folder.getParent() == null ? null : folder.getParent().getId());
        node.setPathId(folder.getPathId());
        node.setName(folder.getText());
        node.setType(folder.getType());
        node.setPath(folder.getPath());
        node.setOwner(folder.getOwner());
        node.setOwnerRealname(folder.getOwnerRealname());
        return node;
    }

    public List<TreeNodeDTO> translateToBussinessAdminTreeNodeDto(List<TreeNodeDTO> folders, String[] nodes) {
        List<TreeNodeDTO> newNodes = new ArrayList<TreeNodeDTO>();
        for (TreeNodeDTO folder : folders) {
            folder.setBussinessAdmin(Arrays.asList(nodes).contains(folder.getId().toString()));
            newNodes.add(folder);
        }
        return newNodes;
    }

}
