package com.eurasia.food.repository;

import com.eurasia.food.entity.Matter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Aaron
 * @date 2020/5/23 - 13:33
 **/
public interface MatterRepository extends JpaRepository<Matter, Integer>, JpaSpecificationExecutor<Matter> {
}
