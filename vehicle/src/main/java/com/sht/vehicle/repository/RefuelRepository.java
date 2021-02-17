package com.sht.vehicle.repository;

import com.sht.vehicle.entity.Refuel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Aaron
 * @date 2021/2/17 16:49
 */
public interface RefuelRepository extends JpaRepository<Refuel, Integer>, JpaSpecificationExecutor<Refuel> {
}
