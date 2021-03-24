package com.eurasia.specialty.repository;

import com.eurasia.specialty.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommentRepository extends JpaRepository<Comment, Integer>, JpaSpecificationExecutor<Comment> {

    Integer countByPostId(Integer postId);

    Boolean existsByUserId(Integer id);
}
