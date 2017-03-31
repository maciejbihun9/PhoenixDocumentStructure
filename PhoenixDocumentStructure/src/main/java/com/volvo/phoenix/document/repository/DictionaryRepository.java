package com.volvo.phoenix.document.repository;

import org.springframework.data.repository.CrudRepository;

import com.volvo.phoenix.document.entity.Dictionary;

public interface DictionaryRepository extends CrudRepository<Dictionary, Long> {

}
