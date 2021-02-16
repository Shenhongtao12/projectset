package com.sht.vehicle.service;

import com.sht.vehicle.common.PageResult;
import com.sht.vehicle.common.RestResponse;
import com.sht.vehicle.entity.Car;
import com.sht.vehicle.repository.CarRepository;
import com.sht.vehicle.utils.JpaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * @author Aaron
 * @date 2021/2/16 11:52
 */
@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public RestResponse save(Car car) {
        if (car.getId() != null && carRepository.existsById(car.getId())) {
            JpaUtils.copyNotNullProperties(car, carRepository.findById(car.getId()).get());
        }else {
            car.setInDate(LocalDate.now());
        }
        try {
            carRepository.save(car);
            return new RestResponse(200, "成功");
        } catch (Exception e) {
            return new RestResponse(400, e.getMessage(), "失败，请稍后重试");
        }
    }

    public PageResult<Car> findByPage(Integer page, Integer size) {

        Page<Car> carPage = carRepository.findAll(PageRequest.of(page, size));
        return new PageResult<>(carPage.getTotalElements(), carPage.getTotalPages(), carPage.getContent());
    }
}
