package com.eu.classroom.repository;

import com.eu.classroom.entity.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Aaron
 * @date 2021/3/11 21:12
 */
public interface BorrowRepository extends JpaRepository<Borrow, Integer>, JpaSpecificationExecutor<Borrow> {

    Boolean existsByUserId(Integer id);

    Boolean existsByEquipmentId(Integer id);
}
