package com.eurasia.food.service;

import com.eurasia.food.repository.CarouselRepository;
import com.eurasia.food.utils.JpaUtils;
import com.eurasia.food.utils.JsonData;
import com.eurasia.food.entity.Carousel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Aaron
 * @date 2020/5/16 - 18:17
 **/
@Service
public class CarouselService {
    @Autowired
    private CarouselRepository carouselRepository;

    public void save(Carousel carousel) {
        carouselRepository.save(carousel);
    }

    public JsonData update(Carousel carousel) {
        Optional<Carousel> carouselOptional = carouselRepository.findById(carousel.getId());
        if (!carouselOptional.isPresent()){
            return JsonData.buildError("数据错误");
        }
        JpaUtils.copyNotNullProperties(carousel, carouselOptional.get());
        carouselRepository.save(carousel);
        return JsonData.buildSuccess("更新成功");
    }

    public List<Carousel> findAll(){
        return carouselRepository.findAll();
    }

    public JsonData delete(Integer id) {
        try {
            carouselRepository.deleteById(id);
            return JsonData.buildSuccess("删除成功");
        } catch (Exception e) {
            return JsonData.buildSuccess("删除失败");
        }
    }
}
