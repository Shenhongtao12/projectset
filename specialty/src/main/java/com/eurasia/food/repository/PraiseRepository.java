package com.eurasia.food.repository;

import com.eurasia.food.entity.Praise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Aaron
 * @date 2020/5/24 - 19:59
 **/
public interface PraiseRepository extends JpaRepository<Praise, Integer> {

    Praise findPraiseByTypeAndTypeIdAndUserId(String type, Integer typeId, Integer userId);

    List<Praise> findByTypeUserId(Integer userId);


    void deleteByTypeAndTypeId(String type, Integer typeId);
}
