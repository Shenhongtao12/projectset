package com.eurasia.food.repository;

import com.eurasia.food.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer>, JpaSpecificationExecutor<Comment> {

    Integer countByPostId(Integer postId);

    List<Comment> findCommentsByMatterIdAndIsShow(Integer matterId, Boolean show);

    Void deleteCommentsByMatterId(Integer id);

}
