package com.sht.shoesboot.controller;

import com.sht.shoesboot.entity.Carousel;
import com.sht.shoesboot.service.CarouselService;
import com.sht.shoesboot.utils.RestResponse;
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
public class CarouselController extends BaseController{

    @Autowired
    private CarouselService carouselService;

    @PostMapping
    public ResponseEntity<RestResponse> save(@RequestBody Carousel carousel) {
        carouselService.save(carousel);
        return ResponseEntity.status(HttpStatus.OK).body(SUCCESS("成功"));
    }

    @PutMapping
    public ResponseEntity<RestResponse> update(@RequestBody Carousel carousel){
        return ResponseEntity.status(HttpStatus.OK).body(carouselService.update(carousel));
    }

    @GetMapping
    public ResponseEntity<RestResponse> query(){
        return ResponseEntity.status(HttpStatus.OK).body(SUCCESS(carouselService.findAll(), ""));
    }

    @DeleteMapping
    public ResponseEntity<RestResponse> delete(@RequestParam(name = "id") Integer id){
        return ResponseEntity.status(HttpStatus.OK).body(carouselService.delete(id));
    }

}
