package com.volvo.phoenix.document.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.volvo.phoenix.document.entity.RootFolderState;
import com.volvo.phoenix.document.entity.RootFolderStatePK;

public interface RootFolderStateRepository extends CrudRepository<RootFolderState, RootFolderStatePK>{
    public List<RootFolderState> findById_NodeId(Long id);
}
