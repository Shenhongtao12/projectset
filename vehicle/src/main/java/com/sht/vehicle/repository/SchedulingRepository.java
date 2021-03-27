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


    @Query(value = "select id from scheduling where ((start_date_time>=?1 and start_date_time<=?2) OR (end_date_time>=?1 and end_date_time<=?2) OR (start_date_time<=?1 and end_date_time>=?2)) and c_id=?3 and status=?4 limit 1" ,nativeQuery = true)
    Integer existsScheduling(LocalDateTime startDateTime, LocalDateTime endDateTime, Integer cId, String s);

    @Query(value = "select status from scheduling where ((start_date_time>=?1 and start_date_time<=?2) OR (end_date_time>=?1 and end_date_time<=?2) OR (start_date_time<=?1 and end_date_time>=?2)) and c_id=?3 and u_id=?4 limit 1" ,nativeQuery = true)
    String existsSchedulingByUser(LocalDateTime startDateTime, LocalDateTime endDateTime, Integer cId, Integer uId);
}
