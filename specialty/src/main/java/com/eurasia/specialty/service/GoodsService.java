package com.eurasia.specialty.service;


import com.eurasia.specialty.entity.Classify;
import com.eurasia.specialty.entity.Goods;
import com.eurasia.specialty.repository.GoodsRepository;
import com.eurasia.specialty.repository.UserRepository;
import com.eurasia.specialty.utils.JpaUtils;
import com.eurasia.specialty.utils.JsonData;
import com.eurasia.specialty.utils.PageResult;
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
import java.util.*;
import java.util.stream.Collectors;


@Service
public class GoodsService {
    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ClassifyService classifyService;

    public Map<String, Object> add(Goods goods) throws Exception {
        Map<String, Object> result = new HashMap<>(2);
        if (goods.getClassifyId() == null) {
            result.put("code", 400);
            result.put("msg", "请选择分类");
            return result;
        }

        goods.setCreateTime(new Date());
        try {
            this.goodsRepository.save(goods);
            result.put("code", 200);
            result.put("msg", "添加成功");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "添加失败");
        }
        return result;
    }

    public Map<String, Object> update(Goods goods) throws Exception {
        Map<String, Object> result = new HashMap<>(2);

        goods.setCreateTime(new Date());
        try {
            Goods info = goodsRepository.findById(goods.getId()).get();
            JpaUtils.copyNotNullProperties(goods, info);
            this.goodsRepository.save(goods);
            result.put("code", 200);
            result.put("msg", "成功");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "失败");
        }
        return result;
    }

    public Goods findById(Integer id, Integer userid) throws Exception {
        return goodsRepository.findById(id).get();
    }

    public PageResult<Goods> findByPage(Integer id, Integer classify, String orderBy,String status, String goodsName, int page, int rows) {
        Specification<Goods> specification = new Specification<Goods>() {
            @Override
            public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if (id != null) {
                    list.add(criteriaBuilder.equal(root.get("id"), id));
                }
                if (classify != null) {
                    list.add(criteriaBuilder.equal(root.get("classify"), classify));
                }
                if (status != null) {
                    list.add(criteriaBuilder.equal(root.get("status"), status));
                }
                if (goodsName != null) {
                    list.add(criteriaBuilder.like(root.get("goodsName"),"%" + goodsName + "%"));
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        Page<Goods> goodsPage = (Page<Goods>) this.goodsRepository.findAll(specification, PageRequest.of(page, rows, Sort.by(Sort.Direction.DESC,orderBy)));
        if (goodsPage.getContent().size() > 0) {
            Map<Integer, String> classifyMap = classifyService.findAll().stream().collect(Collectors.toMap(Classify::getId, Classify::getName));
            goodsPage.getContent().forEach( item -> item.setClassifyName(classifyMap.get(item.getClassifyId())));
        }

        return new PageResult<>(goodsPage.getTotalElements(), goodsPage.getTotalPages(), goodsPage.getContent());
    }


    public JsonData deleteGoods(int id) {
        //删除收藏表中的数据
        // collectService.deleteByGoodsId(id);

        goodsRepository.deleteById(id);
        return JsonData.buildSuccess("删除成功");
    }

}
