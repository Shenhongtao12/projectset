package com.sht.vehicle.service;

import com.sht.vehicle.common.PageResult;
import com.sht.vehicle.common.RestResponse;
import com.sht.vehicle.entity.Scheduling;
import com.sht.vehicle.entity.User;
import com.sht.vehicle.repository.SchedulingRepository;
import com.sht.vehicle.utils.JpaUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Objects;

/**
 * @author Aaron
 * @date 2021/2/20 14:20
 */
@Service
public class SchedulingService {

    @Autowired
    private SchedulingRepository schedulingRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    public RestResponse save(Scheduling scheduling) {
        User byId = userService.findById(scheduling.getUId());
        if (!byId.getDriver()) {
            return new RestResponse(400, "非驾驶人员，不能借用车辆");
        }
        if (scheduling.getId() != null && schedulingRepository.existsById(scheduling.getId())) {
            JpaUtils.copyNotNullProperties(scheduling, schedulingRepository.findById(scheduling.getId()).get());
        }else {
            if (scheduling.getStartDateTime().isAfter(scheduling.getEndDateTime())) {
                return new RestResponse(400, "开始时间不能小于结束时间");
            }
            if (scheduling.getStartDateTime().isBefore(LocalDateTime.now())) {
                return new RestResponse(400, "预约时间不能小于当前时间");
            }
            if (schedulingRepository.existsScheduling(scheduling.getStartDateTime(), scheduling.getEndDateTime(),scheduling.getCId(), "2") != null) {
                return new RestResponse(400, "该时间段已经有人预约");
            }
            String byUser = schedulingRepository.existsSchedulingByUser(scheduling.getStartDateTime(), scheduling.getEndDateTime(), scheduling.getCId(), scheduling.getUId());
            if (byUser != null) {
                return new RestResponse(400, "您已预约该时间段！");
            }
            scheduling.setStatus("1");
        }

        try {
            schedulingRepository.save(scheduling);
            return new RestResponse(200, "成功");
        } catch (Exception e) {
            return new RestResponse(400, e.getMessage(), "失败");
        }

    }

    public RestResponse delete(Integer id) {
        try {
            schedulingRepository.deleteById(id);
            return new RestResponse(200, "删除成功");
        }catch (Exception e) {
            return new RestResponse(400, "删除失败");
        }
    }

    public PageResult<Scheduling> findByPage(LocalDateTime startDate, LocalDateTime endDate, Integer userId, Integer carId, String status, Integer page, Integer size) {
        Specification<Scheduling> spec = new Specification<Scheduling>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if (Objects.nonNull(startDate)) {
                    list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startDateTime"), startDate));
                }
                if (Objects.nonNull(endDate)) {
                    list.add(criteriaBuilder.lessThanOrEqualTo(root.get("endDateTime"), endDate));
                }

                if (userId != null) {
                    list.add(criteriaBuilder.equal(root.get("uId"), userId));
                }

                if (!Objects.isNull(carId)) {
                    list.add(criteriaBuilder.equal(root.get("cId"), carId));
                }
                if (!Objects.isNull(status)) {
                    list.add(criteriaBuilder.equal(root.get("status"), status));
                }

                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        Page<Scheduling> refuelPage = schedulingRepository.findAll(spec, PageRequest.of(page, size));
        refuelPage.getContent().forEach(x -> {
            x.setUser(userService.findById(x.getUId()));
            x.setCar(carService.findById(x.getCId()));
        });
        return new PageResult<>(refuelPage.getTotalElements(), refuelPage.getTotalPages(), refuelPage.getContent());
    }

    public RestResponse approval(Integer id, String operation) {
        if (!schedulingRepository.existsById(id)) {
            return new RestResponse(400, "不存在该id");
        }
        Scheduling scheduling = schedulingRepository.findById(id).get();
        if ("1".equals(operation)) {
            if (schedulingRepository.existsScheduling(scheduling.getStartDateTime(), scheduling.getEndDateTime(),scheduling.getCId(), "2") != null) {
                scheduling.setStatus("3");
                schedulingRepository.save(scheduling);
                return new RestResponse(400, "该时间段已经有人成功预约,自动审批为拒绝");
            } else {
                scheduling.setStatus("2");
            }
        }else {
            scheduling.setStatus("3");
        }
        schedulingRepository.save(scheduling);
        return new RestResponse(200, "成功");
    }
}
