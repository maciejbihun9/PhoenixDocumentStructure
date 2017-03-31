package com.volvo.phoenix.document.repository;

import org.springframework.data.repository.CrudRepository;

import com.volvo.phoenix.document.entity.DocumentAttribute;
import com.volvo.phoenix.document.entity.DocumentAttributePK;

public interface DocumentAttributeRepository extends CrudRepository<DocumentAttribute, DocumentAttributePK> {

}
