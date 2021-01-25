package com.eurasia.specialty.controller;

import com.eurasia.specialty.entity.Carousel;
import com.eurasia.specialty.service.CarouselService;
import com.eurasia.specialty.utils.JsonData;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Aaron
 * @date 2020/5/16 - 18:14
 **/
@RestController
@RequestMapping("api/carousel")
@Api(tags = "轮播图")
public class CarouselController {

    @Autowired
    private CarouselService carouselService;

    @PostMapping("save")
    public ResponseEntity<JsonData> save(@RequestBody Carousel carousel) {
        carouselService.save(carousel);
        return ResponseEntity.status(HttpStatus.OK).body(JsonData.buildSuccess("成功"));
    }

    @PutMapping("update")
    public ResponseEntity<JsonData> update(@RequestBody Carousel carousel){
        return ResponseEntity.status(HttpStatus.OK).body(carouselService.update(carousel));
    }

    @GetMapping
    public ResponseEntity<JsonData> query(){
        return ResponseEntity.status(HttpStatus.OK).body(JsonData.buildSuccess(carouselService.findAll(), ""));
    }

    @DeleteMapping
    public ResponseEntity<JsonData> delete(@RequestParam(name = "id") Integer id){
        return ResponseEntity.status(HttpStatus.OK).body(carouselService.delete(id));
    }

}
