package com.eurasia.food.repository;

import com.eurasia.food.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Integer>, JpaSpecificationExecutor<Reply> {
    List<Reply> findByCommentId(Integer id);

    int deleteRepliesByCommentId(Integer id);

    Integer countByParentId(Integer id);

    Integer deleteRepliesByParentId(Integer id);

    List<Reply> findRepliesByCommentId(Integer id);
}
