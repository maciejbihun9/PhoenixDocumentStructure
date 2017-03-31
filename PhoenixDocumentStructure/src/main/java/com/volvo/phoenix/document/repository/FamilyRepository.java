package com.volvo.phoenix.document.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.volvo.phoenix.document.entity.Family;

public interface FamilyRepository extends JpaRepository<Family, Long> {}