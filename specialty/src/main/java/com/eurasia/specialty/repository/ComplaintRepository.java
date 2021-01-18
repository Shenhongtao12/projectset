package com.eurasia.specialty.repository;

import com.eurasia.specialty.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Aaron
 * @date 2020/6/8 - 20:45
 **/
public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {
    List<Complaint> findByUserId(Integer userId);
}
