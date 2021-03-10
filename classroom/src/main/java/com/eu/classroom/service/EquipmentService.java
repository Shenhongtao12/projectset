package com.eu.classroom.service;

import cn.hutool.json.JSONObject;
import com.eu.classroom.common.RestResponse;
import com.eu.classroom.entity.Equipment;
import com.eu.classroom.entity.User;
import com.eu.classroom.repository.EquipmentRepository;
import com.eu.classroom.utils.JpaUtils;
import com.eu.classroom.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Aaron
 * @date 2021/3/10 22:58
 */
@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

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
}
