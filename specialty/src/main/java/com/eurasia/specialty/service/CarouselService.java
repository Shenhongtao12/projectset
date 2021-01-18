package com.eurasia.specialty.service;

import com.eurasia.specialty.repository.CarouselRepository;
import com.eurasia.specialty.entity.Carousel;
import com.eurasia.specialty.utils.JpaUtils;
import com.eurasia.specialty.utils.JsonData;
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
}
