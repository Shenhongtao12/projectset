package com.sht.vehicle.repository;

import com.sht.vehicle.entity.Car;
import com.sht.vehicle.entity.Upkeep;
import com.sht.vehicle.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author Aaron
 * @date 2021/2/17 16:49
 */
public interface UpkeepRepository extends JpaRepository<Upkeep, Integer>, JpaSpecificationExecutor<Upkeep> {
    List<Upkeep> findAllByCarEquals(Car byId);

    List<Upkeep> findAllByUser(User user);
}
