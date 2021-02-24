package com.sht.vehicle.repository;

import com.sht.vehicle.entity.Car;
import com.sht.vehicle.entity.Refuel;
import com.sht.vehicle.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author Aaron
 * @date 2021/2/17 16:49
 */
public interface RefuelRepository extends JpaRepository<Refuel, Integer>, JpaSpecificationExecutor<Refuel> {

    List<Refuel> findAllByCarEquals(Car car);

    List<Refuel> findAllByUser(User user);
}
