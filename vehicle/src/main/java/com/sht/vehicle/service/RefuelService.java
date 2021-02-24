package com.sht.vehicle.service;

import com.sht.vehicle.common.PageResult;
import com.sht.vehicle.common.RestResponse;
import com.sht.vehicle.entity.Refuel;
import com.sht.vehicle.repository.RefuelRepository;
import com.sht.vehicle.repository.UserRepository;
import com.sht.vehicle.utils.JpaUtils;
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
 * @date 2021/2/17 16:49
 */
@Service
public class RefuelService {

    @Autowired
    private RefuelRepository refuelRepository;

    @Autowired
    private CarService carService;
    @Autowired
    private UserRepository userRepository;

    public RestResponse save(Refuel refuel) {
        if (refuel.getId() != null && refuelRepository.existsById(refuel.getId())) {
            JpaUtils.copyNotNullProperties(refuel, refuelRepository.findById(refuel.getId()).get());
        }

        try {
            refuelRepository.save(refuel);
            return new RestResponse(200, "成功");
        } catch (Exception e) {
            return new RestResponse(400, e.getMessage(), "失败");
        }

    }

    public RestResponse delete(Integer id) {
        try {
            refuelRepository.deleteById(id);
            return new RestResponse(200, "删除成功");
        }catch (Exception e) {
            return new RestResponse(400, "删除失败");
        }
    }

    public PageResult<Refuel> findByPage(LocalDateTime startDate, LocalDateTime endDate, Integer userId, Integer carId, Integer page, Integer size) {
        Specification<Refuel> spec = new Specification<Refuel>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if (Objects.nonNull(startDate)) {
                    list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startDate));
                }
                if (Objects.nonNull(endDate)) {
                    list.add(criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endDate));
                }

                if (Objects.nonNull(carId)) {
                    list.add(criteriaBuilder.equal(root.get("car").get("id"), carId));
                }

                if (Objects.nonNull(userId)) {
                    list.add(criteriaBuilder.equal(root.get("user").get("id"), userId));
                }

                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        Page<Refuel> refuelPage = refuelRepository.findAll(spec, PageRequest.of(page, size));
        return new PageResult<>(refuelPage.getTotalElements(), refuelPage.getTotalPages(), refuelPage.getContent());
    }

    public RestResponse findById(Integer id, String type) {
        List<Refuel> refuels = new ArrayList<>();
        if ("car".equals(type) && carService.exists(id)) {
            refuels = refuelRepository.findAllByCarEquals(carService.findById(id));
        }
        if ("user".equals(type) && userRepository.existsById(id)){
            refuels = refuelRepository.findAllByUser(userRepository.findById(id).get());
        }
        return new RestResponse(200, refuels);
    }
}
