package com.sht.vehicle.repository;

import com.sht.vehicle.entity.Scheduling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

/**
 * @author Aaron
 * @date 2021/2/20 14:20
 */
public interface SchedulingRepository extends JpaRepository<Scheduling, Integer>, JpaSpecificationExecutor<Scheduling> {


    @Query(value = "select scheduling0_.id as col_0_0_ from scheduling scheduling0_ where scheduling0_.start_date_time>?1 and scheduling0_.end_date_time<?2 and c_id=?3 and scheduling0_.status=?4 limit 1" ,nativeQuery = true)
    Integer existsScheduling(LocalDateTime startDateTime, LocalDateTime endDateTime, Integer cId, String s);
}
