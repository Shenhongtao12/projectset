package com.eu.classroom.repository;

import com.eu.classroom.entity.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

/**
 * @author Aaron
 * @date 2021/3/12 21:47
 */
public interface ReserveRepository extends JpaRepository<Reserve, Integer>, JpaSpecificationExecutor<Reserve> {
    /**
     *  check exists
     * @param startDateTime
     * @param endDateTime
     * @param laboratoryId
     * @param s
     * @return
     */
    @Query(value = "select id from reserve where ((start_date_time>=?1 and start_date_time<=?2) OR (end_date_time>=?1 and end_date_time<=?2) OR (start_date_time<=?1 and end_date_time>=?2)) and laboratory_id=?3 and status=?4 limit 1" ,nativeQuery = true)
    Integer existsReserve(LocalDateTime startDateTime, LocalDateTime endDateTime, Integer laboratoryId, Integer s);

    @Query(value = "select status from reserve where ((start_date_time>=?1 and start_date_time<=?2) OR (end_date_time>=?1 and end_date_time<=?2) OR (start_date_time<=?1 and end_date_time>=?2)) and laboratory_id=?3 and user_id=?4 limit 1" ,nativeQuery = true)
    Integer existsReserveByUser(LocalDateTime startDateTime, LocalDateTime endDateTime, Integer laboratoryId, Integer userId);

    Boolean existsByUserId(Integer id);

    Boolean existsByLaboratoryId(Integer id);
}
