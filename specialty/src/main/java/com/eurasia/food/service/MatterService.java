package com.eurasia.food.service;

import com.eurasia.food.repository.MatterRepository;
import com.eurasia.food.utils.DateUtils;
import com.eurasia.food.utils.JpaUtils;
import com.eurasia.food.utils.JsonData;
import com.eurasia.food.utils.PageResult;
import com.eurasia.food.entity.Matter;
import org.apache.commons.lang3.StringUtils;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aaron
 * @date 2020/5/23 - 13:33
 **/
@Service
public class MatterService {
    @Autowired
    private MatterRepository matterRepository;


    public JsonData save(Matter matter) {
        try {
            if (matter.getId() == null) {
                matter.setCreateTime(DateUtils.dateToString());
                Matter save = matterRepository.save(matter);
            } else {
                Matter one = matterRepository.getOne(matter.getId());
                JpaUtils.copyNotNullProperties(matter, one);
                matterRepository.save(matter);
            }
            return JsonData.buildSuccess("成功");
        } catch (Exception e) {
            return JsonData.buildError("发生异常，请稍后重试");
        }
    }

    public JsonData delete(Integer id) {
        if (!matterRepository.existsById(id)) {
            return JsonData.buildError("不存在的资源");
        }
        matterRepository.deleteById(id);
        return JsonData.buildSuccess("成功");
    }

    public PageResult<Matter> findByPage(Integer id, String title, Integer page, Integer rows) {
        Specification<Matter> specification = new Specification<Matter>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if (id != null) {
                    list.add(criteriaBuilder.equal(root.get("id"), id));
                }
                if(id == null && StringUtils.isNotEmpty(title)){
                    list.add(criteriaBuilder.equal(root.get("title"), title));
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };

        Page<Matter> matterPage = matterRepository.findAll(specification, PageRequest.of(page, rows, Sort.by(Sort.Direction.DESC,"id")));
        return new PageResult<>(matterPage.getTotalElements(), matterPage.getTotalPages(), matterPage.getContent());
    }
}
