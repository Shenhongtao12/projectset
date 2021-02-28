package com.sht.shoesboot.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sht.shoesboot.entity.Classify;
import com.sht.shoesboot.entity.PageResult;
import com.sht.shoesboot.mapper.ClassifyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;

/**
 * @author Aaron
 * @date 2020/12/27 19:47
 */
@Service
public class ClassifyService {

    @Autowired
    private ClassifyMapper classifyMapper;

    public boolean save(Classify classify) {
        Example example = new Example(Classify.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", classify.getName());
        if (classifyMapper.selectOneByExample(example) != null) {
            return false;
        } else {
            classify.setInDate(LocalDateTime.now());
            classifyMapper.insertSelective(classify);
            return true;
        }
    }

    public PageResult<Classify> queryPage(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        Page<Classify> classifies = (Page<Classify>) classifyMapper.selectAll();
        return new PageResult<>(classifies.getTotal(), classifies.getPages(), classifies.getResult());
    }

    public boolean update(Classify classify) {
        if (classifyMapper.existsWithPrimaryKey(classify.getId())){
            classifyMapper.updateByPrimaryKeySelective(classify);
            return true;
        }
        return false;
    }

    public void delete(Integer id) {
        classifyMapper.deleteByPrimaryKey(id);
    }
}
