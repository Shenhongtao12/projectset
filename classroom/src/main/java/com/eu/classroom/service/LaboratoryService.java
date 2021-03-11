package com.eu.classroom.service;

import com.eu.classroom.common.PageResult;
import com.eu.classroom.common.RestResponse;
import com.eu.classroom.entity.Laboratory;
import com.eu.classroom.repository.LaboratoryRepository;
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
 * @date 2021/3/11 22:49
 */
@Slf4j
@Service
public class LaboratoryService {
    @Autowired
    private LaboratoryRepository laboratoryRepository;

    public RestResponse saveOrUpdate(Laboratory laboratory) {
        RestResponse response = new RestResponse();
        if (laboratory.getId() != null && laboratoryRepository.existsById(laboratory.getId())) {
            Laboratory one = laboratoryRepository.getOne(laboratory.getId());
            JpaUtils.copyNotNullProperties(laboratory, one);
        } else {
            laboratory.setInDate(LocalDateTime.now());
        }
        try {
            Laboratory save = laboratoryRepository.save(laboratory);
            response.setCode(200);
            response.setMessage("成功");
            response.setData(save);
            return response;
        } catch (Exception e) {
            log.info(e.getMessage());
            response.setCode(500);
            response.setData(e.getMessage());
            response.setMessage("服务异常，请稍后重试");
            return response;
        }
    }

    public PageResult<Laboratory> findByPage(String name, Integer page, Integer size) {
        Specification<Laboratory> spec = new Specification<Laboratory>() {
            @Override
            public Predicate toPredicate(Root<Laboratory> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> list = new ArrayList<>();
                if (name != null) {
                    list.add(criteriaBuilder.like(root.get("name"), "%"+name+"%"));
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        Page<Laboratory> userPage = laboratoryRepository.findAll(spec, PageRequest.of(page, size));
        return new PageResult<>(userPage.getTotalElements(), userPage.getTotalPages(), userPage.getContent());
    }

    public RestResponse delete(Integer id) {
        if (!laboratoryRepository.existsById(id)) {
            return new RestResponse(400, "不存在该id");
        }
        laboratoryRepository.deleteById(id);
        return new RestResponse(200, "删除成功");
    }

    public Laboratory findById(Integer id) {
        try {
            return laboratoryRepository.findById(id).get();
        } catch (Exception e) {
            log.info(e.getMessage());
            return new Laboratory();
        }
    }

    public void updateBorrowNum(Laboratory laboratory) {
        laboratoryRepository.save(laboratory);
    }
}
