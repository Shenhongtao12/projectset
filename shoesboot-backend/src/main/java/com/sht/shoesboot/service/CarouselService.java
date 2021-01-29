package com.sht.shoesboot.service;

import com.sht.shoesboot.entity.Carousel;
import com.sht.shoesboot.mapper.CarouselMapper;
import com.sht.shoesboot.utils.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Aaron
 * @date 2020/5/16 - 18:17
 **/
@Service
public class CarouselService {
    @Autowired
    private CarouselMapper carouselMapper;

    public void save(Carousel carousel) {
        carouselMapper.insertSelective(carousel);
    }

    public RestResponse update(Carousel carousel) {
        carouselMapper.updateByPrimaryKeySelective(carousel);
        return new RestResponse(200,"更新成功");
    }

    public List<Carousel> findAll(){
        return carouselMapper.selectAll();
    }

    public RestResponse delete(Integer id) {
        try {
            carouselMapper.deleteByPrimaryKey(id);
            return new RestResponse(200, "删除成功");
        } catch (Exception e) {
            return new RestResponse(400,"删除失败");
        }
    }
}
