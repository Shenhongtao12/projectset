package com.eu.classroom.service;

import cn.hutool.core.util.StrUtil;
import com.eu.classroom.common.PageResult;
import com.eu.classroom.common.RestResponse;
import com.eu.classroom.entity.Announcement;
import com.eu.classroom.repository.AnnouncementRepository;
import com.eu.classroom.utils.JpaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
 * @date 2021/3/10 20:53
 */
@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    public RestResponse saveOrUpdate(Announcement announcement) {
        if (announcement.getId() != null && announcementRepository.existsById(announcement.getId())) {
            JpaUtils.copyNotNullProperties(announcement, announcementRepository.findById(announcement.getId()).get());
        }else {
            announcement.setInDate(LocalDateTime.now());
        }

        try {
            announcementRepository.save(announcement);
            return new RestResponse(200, "成功");
        } catch (Exception e) {
            return new RestResponse(400, e.getMessage(), "失败");
        }
    }

    public PageResult<Announcement> findByPage(String status, Integer page, Integer size) {
        Specification<Announcement> specification = new Specification<Announcement>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if (Objects.nonNull(status)) {
                    list.add(criteriaBuilder.equal(root.get("status"), StrUtil.equals("1", status)));
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };

        Page<Announcement> annPage = announcementRepository.findAll(specification, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "inDate")));
        return new PageResult<Announcement>(annPage.getTotalElements(),annPage.getTotalPages(), annPage.getContent());
    }

    public RestResponse delete(Integer id) {
        if (!announcementRepository.existsById(id)) {
            return new RestResponse(400, "不存在该id: " + id);
        }
        try {
            announcementRepository.deleteById(id);
            return new RestResponse(200, "删除成功");
        }catch (Exception e) {
            return new RestResponse(400, "服务出错，请稍后再试");
        }
    }
}
