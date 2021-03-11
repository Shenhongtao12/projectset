package com.eu.classroom.repository;

import com.eu.classroom.entity.Laboratory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Aaron
 * @date 2021/3/11 22:49
 */
public interface LaboratoryRepository extends JpaRepository<Laboratory, Integer>, JpaSpecificationExecutor<Laboratory> {
}
