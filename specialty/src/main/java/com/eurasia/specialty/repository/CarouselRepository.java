package com.eurasia.specialty.repository;

import com.eurasia.specialty.entity.Carousel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Aaron
 * @date 2020/5/16 - 18:17
 **/
public interface CarouselRepository extends JpaRepository<Carousel, Integer>, JpaSpecificationExecutor<Carousel> {
}
