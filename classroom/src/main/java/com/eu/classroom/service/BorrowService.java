package com.eu.classroom.service;

import cn.hutool.core.util.StrUtil;
import com.eu.classroom.common.PageResult;
import com.eu.classroom.common.RestResponse;
import com.eu.classroom.dto.BorrowReq;
import com.eu.classroom.entity.Borrow;
import com.eu.classroom.entity.Equipment;
import com.eu.classroom.repository.BorrowRepository;
import com.eu.classroom.utils.JpaUtils;
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
 * @date 2021/3/11 21:11
 */
@Service
@Slf4j
public class BorrowService {

    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private UserService userService;

    public RestResponse saveOrUpdate(Borrow equipment) {
        RestResponse response = new RestResponse();
        if (equipment.getId() != null && borrowRepository.existsById(equipment.getId())) {
            Borrow one = borrowRepository.getOne(equipment.getId());
            JpaUtils.copyNotNullProperties(equipment, one);
        } else {
            equipment.setStatus(1);
            equipment.setInDate(LocalDateTime.now());
        }
        try {
            Borrow save = borrowRepository.save(equipment);
            response.setCode(200);
            response.setMessage("成功");
            return response;
        } catch (Exception e) {
            response.setCode(500);
            response.setData(e.getMessage());
            response.setMessage("服务异常，请稍后重试");
            return response;
        }
    }

    public PageResult<Borrow> findByPage(Integer userId, Integer equipmentId, Integer page, Integer size) {
        Specification<Borrow> spec = new Specification<Borrow>() {
            @Override
            public Predicate toPredicate(Root<Borrow> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if (userId != null) {
                    list.add(criteriaBuilder.equal(root.get("userId"), userId));
                }
                if (equipmentId != null) {
                    list.add(criteriaBuilder.equal(root.get("equipmentId"), equipmentId));
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        Page<Borrow> borrowPage = borrowRepository.findAll(spec, PageRequest.of(page, size));
        if (!borrowPage.getContent().isEmpty()) {
            if (userId == null && equipmentId == null) {
                borrowPage.getContent().forEach(x -> {
                    x.setEquipment(equipmentService.findById(x.getEquipmentId()));
                    x.setUser(userService.findById(x.getUserId()));
                });
            } else {
                if (userId != null) {
                    borrowPage.getContent().forEach(x -> {
                        x.setEquipment(equipmentService.findById(x.getEquipmentId()));
                    });
                }
                if (equipmentId != null) {
                    borrowPage.getContent().forEach(x -> {
                        x.setUser(userService.findById(x.getUserId()));
                    });
                }
            }
        }
        return new PageResult<>(borrowPage.getTotalElements(), borrowPage.getTotalPages(), borrowPage.getContent());
    }

    public RestResponse delete(Integer id) {
        if (!borrowRepository.existsById(id)) {
            return new RestResponse(400, "不存在该id");
        }
        borrowRepository.deleteById(id);
        return new RestResponse(200, "删除成功");
    }

    public RestResponse approval(BorrowReq borrowReq) {
        if (!borrowRepository.existsById(borrowReq.getId())) {
            return new RestResponse(400, "不存在该id");
        }
        Borrow one = borrowRepository.getOne(borrowReq.getId());
        Equipment equipment = equipmentService.findById(one.getEquipmentId());
        if (equipment.getInventory() < one.getBorrowNum()) {
            return new RestResponse(400, "库存不足");
        }

        //已经同意以后再次审批为拒绝
        if (one.getStatus() == 2 && !StrUtil.equals("1", borrowReq.getOperation())) {
            equipment.setBorrowNum(equipment.getBorrowNum() - one.getBorrowNum());
            equipment.setInventory(equipment.getInventory() + one.getBorrowNum());
        }
        //正常审批同意
        if (one.getStatus() != 2 && StrUtil.equals("1", borrowReq.getOperation())){
            equipment.setBorrowNum(equipment.getBorrowNum() + one.getBorrowNum());
            equipment.setInventory(equipment.getInventory() - one.getBorrowNum());
        }
        log.info(equipment.toString());
        //更新库存
        equipmentService.updateBorrowNum(equipment);

        one.setStatus(StrUtil.equals("1", borrowReq.getOperation()) ? 2 : 3);
        borrowRepository.save(one);
        return new RestResponse(200, "审批成功");
    }

    public RestResponse refund(Integer id) {
        if (!borrowRepository.existsById(id)){
            return new RestResponse(400, "不存在id");
        }
        Borrow borrow = borrowRepository.findById(id).get();
        //更新借阅
        borrow.setStatus(4);
        borrow.setEndDate(LocalDateTime.now());
        //更新器材库存
        Equipment equipment = equipmentService.findById(borrow.getEquipmentId());
        equipment.setBorrowNum(equipment.getBorrowNum() - borrow.getBorrowNum());
        equipment.setInventory(equipment.getInventory() + borrow.getBorrowNum());
        //更新库存
        equipmentService.updateBorrowNum(equipment);

        borrowRepository.save(borrow);
        return new RestResponse(200, "归还成功");
    }

    public Boolean existsByUser(Integer id){
        return borrowRepository.existsByUserId(id);
    }

    public Boolean existsByEq(Integer id){
        return borrowRepository.existsByEquipmentId(id);
    }
}
