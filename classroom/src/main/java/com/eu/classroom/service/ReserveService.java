package com.eu.classroom.service;

import cn.hutool.core.util.StrUtil;
import com.eu.classroom.common.PageResult;
import com.eu.classroom.common.RestResponse;
import com.eu.classroom.dto.BorrowReq;
import com.eu.classroom.entity.Reserve;
import com.eu.classroom.entity.Laboratory;
import com.eu.classroom.repository.ReserveRepository;
import com.eu.classroom.utils.JpaUtils;
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
 * @date 2021/3/12 21:47
 */
@Service
public class ReserveService {

    @Autowired
    private ReserveRepository reserveRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private LaboratoryService laboratoryService;

    public RestResponse saveOrUpdate(Reserve reserve) {
        if (reserve.getId() != null && reserveRepository.existsById(reserve.getId())) {
            JpaUtils.copyNotNullProperties(reserve, reserveRepository.findById(reserve.getId()).get());
        }else {
            if (laboratoryService.findById(reserve.getLaboratoryId()).getStatus()) {
                return new RestResponse(400, "该实验室已停用");
            }
            if (reserve.getStartDateTime().isAfter(reserve.getEndDateTime())) {
                return new RestResponse(400, "开始时间不能小于结束时间");
            }
            if (reserve.getStartDateTime().isBefore(LocalDateTime.now())) {
                return new RestResponse(400, "预约时间不能小于当前时间");
            }
            if (reserveRepository.existsReserve(reserve.getStartDateTime(), reserve.getEndDateTime(),reserve.getLaboratoryId(), 2) != null) {
                return new RestResponse(400, "该时间段已经有人预约");
            }
            Integer byUser = reserveRepository.existsReserveByUser(reserve.getStartDateTime(), reserve.getEndDateTime(), reserve.getLaboratoryId(), reserve.getUserId());
            if (byUser != null) {
                return new RestResponse(400, "该时间段与您预约过的时间段冲突");
            }
            reserve.setStatus(1);
            reserve.setInDate(LocalDateTime.now());
        }

        try {
            reserveRepository.save(reserve);
            return new RestResponse(200, "成功");
        } catch (Exception e) {
            return new RestResponse(400, e.getMessage(), "失败");
        }

    }

    public PageResult<Reserve> findByPage(Integer userId, Integer equipmentId, Integer page, Integer size) {
        Specification<Reserve> spec = new Specification<Reserve>() {
            @Override
            public Predicate toPredicate(Root<Reserve> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
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
        Page<Reserve> borrowPage = reserveRepository.findAll(spec, PageRequest.of(page, size));
        if (!borrowPage.getContent().isEmpty()) {
            if (userId == null && equipmentId == null) {
                borrowPage.getContent().forEach(x -> {
                    x.setLaboratory(laboratoryService.findById(x.getLaboratoryId()));
                    x.setUser(userService.findById(x.getUserId()));
                });
            } else {
                if (userId != null) {
                    borrowPage.getContent().forEach(x -> {
                        x.setLaboratory(laboratoryService.findById(x.getLaboratoryId()));
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
        if (!reserveRepository.existsById(id)) {
            return new RestResponse(400, "不存在该id");
        }
        reserveRepository.deleteById(id);
        return new RestResponse(200, "删除成功");
    }

    public RestResponse approval(BorrowReq borrowReq) {
        if (!reserveRepository.existsById(borrowReq.getId())) {
            return new RestResponse(400, "不存在该id");
        }
        Reserve reserve = reserveRepository.findById(borrowReq.getId()).get();
        if ("1".equals(borrowReq.getOperation())) {
            if (reserve.getStatus() == 2) {
                return new RestResponse(400, "您已审批通过，切勿重复操作");
            }
            if (reserveRepository.existsReserve(reserve.getStartDateTime(), reserve.getEndDateTime(),reserve.getLaboratoryId(), 2) != null) {
                reserve.setStatus(3);
                reserveRepository.save(reserve);
                return new RestResponse(400, "该时间段已经有人成功预约,自动审批为拒绝");
            } else {
                reserve.setStatus(2);
            }
        }else {
            reserve.setStatus(3);
        }
        reserveRepository.save(reserve);
        return new RestResponse(200, "审批成功");
    }

    public Boolean existsByUser(Integer id){
        return reserveRepository.existsByUserId(id);
    }

    public Boolean existsByLa(Integer id){
        return reserveRepository.existsByLaboratoryId(id);
    }
}
