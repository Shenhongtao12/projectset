package com.eu.classroom.service;

import cn.hutool.json.JSONObject;
import com.eu.classroom.common.PageResult;
import com.eu.classroom.common.RestResponse;
import com.eu.classroom.entity.Equipment;
import com.eu.classroom.entity.User;
import com.eu.classroom.repository.EquipmentRepository;
import com.eu.classroom.utils.JpaUtils;
import com.eu.classroom.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aaron
 * @date 2021/3/10 22:58
 */
@Slf4j
@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private BorrowService borrowService;

    public RestResponse saveOrUpdate(Equipment equipment) {
        RestResponse response = new RestResponse();
        if (equipment.getId() != null && equipmentRepository.existsById(equipment.getId())) {
            Equipment one = equipmentRepository.getOne(equipment.getId());
            JpaUtils.copyNotNullProperties(equipment, one);
        } else {
            equipment.setInDate(LocalDateTime.now());
        }
        try {
            Equipment save = equipmentRepository.save(equipment);
            response.setCode(200);
            response.setMessage("成功");
            response.setData(save);
            return response;
        } catch (Exception e) {
            response.setCode(500);
            response.setData(e.getMessage());
            response.setMessage("服务异常，请稍后重试");
            return response;
        }
    }

    public PageResult<Equipment> findByPage(String name, Integer page, Integer size) {
        Specification<Equipment> spec = new Specification<Equipment>() {
            @Override
            public Predicate toPredicate(Root<Equipment> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> list = new ArrayList<>();
                if (name != null) {
                    list.add(criteriaBuilder.like(root.get("name"), "%"+name+"%"));
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        Page<Equipment> userPage = equipmentRepository.findAll(spec, PageRequest.of(page, size));
        return new PageResult<>(userPage.getTotalElements(), userPage.getTotalPages(), userPage.getContent());
    }

    public RestResponse delete(Integer id) {
        if (borrowService.existsByEq(id)) {
            return new RestResponse(400, "该器材存在借用记录，不能删除");
        }
        if (!equipmentRepository.existsById(id)) {
            return new RestResponse(400, "不存在该id");
        }
        equipmentRepository.deleteById(id);
        return new RestResponse(200, "删除成功");
    }

    public Equipment findById(Integer id) {
        try {
            return equipmentRepository.findById(id).get();
        } catch (Exception e) {
            log.info(e.getMessage());
            return new Equipment();
        }
    }

    public void updateBorrowNum(Equipment equipment) {
        equipmentRepository.save(equipment);
    }
}
