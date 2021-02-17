package com.eurasia.food.repository;

import com.eurasia.food.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author Aaron
 * @date 2020/5/17 - 19:11
 **/
public interface PostRepository extends JpaRepository<Post, Integer>, JpaSpecificationExecutor<Post> {

    Post findUserIdById(Integer postId);

    List<Post> findByUserId(Integer userId);
}
