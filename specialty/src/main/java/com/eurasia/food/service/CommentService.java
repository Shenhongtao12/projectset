package com.eurasia.food.service;

import com.eurasia.food.entity.Reply;
import com.eurasia.food.repository.CommentRepository;
import com.eurasia.food.utils.DateUtils;
import com.eurasia.food.utils.JsonData;
import com.eurasia.food.utils.PageResult;
import com.eurasia.food.entity.Comment;
import com.eurasia.food.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Id;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ReplyService replyService;
    @Autowired
    private PostService postService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserService userService;

    @Autowired
    private MatterService matterService;

    //添加留言
    public JsonData save(Comment comment) {
        if (comment.getMatterId() != null) {
            comment.setIsShow(false);
        }else {
            comment.setMatterId(0);
        }
        comment.setCreateTime(DateUtils.dateToString());
        comment.setNumber(0);
        commentRepository.save(comment);
        if (comment.getPostId() != null) {
            Post post = postService.findUserIdByPostId(comment.getPostId());
            if (!comment.getUserId().equals(post.getUserId())) {
                this.redisTemplate.boundValueOps("eurasia_" + post.getUserId()).increment(1);
            }
        }
        return JsonData.buildSuccess("成功");
    }

    //删除留言和留言的回复
    @Transactional
    public JsonData delete(Integer id) {
        int num = replyService.deleteByCommentId(id);
        commentRepository.deleteById(id);
        return JsonData.buildSuccess("删除成功,并删除"+ num + "条回复内容");
    }

    @Transactional
    public void deleteByMatterId(Integer id) {
        commentRepository.deleteCommentsByMatterId(id);
    }

    //根据postId查询留言回复
    public List<Comment> findByPostId(Integer postId, Integer userId, String sortName){
        Specification<Comment> spec = new Specification<Comment>() {
            @Override
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                //根据属性名获取查询对象的属性
                list.add(criteriaBuilder.equal(root.get("postId"), postId));
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        List<Comment> commentList = commentRepository.findAll(spec, Sort.by(Sort.Direction.DESC, sortName));
        for (Comment comment : commentList) {
            comment.setUser(userService.findUserById(comment.getUserId()));
            comment.setReplyList(replyService.getTreeReply(comment.getCommentId(), userId));
           }
        return commentList;
    }

    //根据commentId 查询一个留言下的回复
    public Comment findOneComment(Integer id, Integer userId){
        Comment comment = findById(id);
        comment.setReplyList(replyService.getTreeReply(comment.getCommentId(), userId));
        return comment;
    }

    //查看与我相关，回复留言
    public List<Comment> findByPostIdUserId(Integer postId, Integer userId){
        Specification<Comment> spec = new Specification<Comment>() {
            @Override
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                //根据属性名获取查询对象的属性
                list.add(criteriaBuilder.equal(root.get("postId"), postId));
                list.add(criteriaBuilder.notEqual(root.get("userId"), userId));
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        return commentRepository.findAll(spec);
    }

    //findById
    public Comment findById(Integer id){
        return commentRepository.findById(id).get();
    }

    /**
     * 根据postId查找对应的commentNum
     * @param postId post id
     * @return result
     */
    public Integer countByPostId(Integer postId){
        return commentRepository.countByPostId(postId);
    }

    public PageResult<Comment> findMessagePage(Integer page, Integer rows) {
        Page<Comment> commentPage = commentRepository.findAll(PageRequest.of(page, rows));
        commentPage.getContent().forEach(item -> item = findOneComment(item.getCommentId(), item.getUserId()));
        return new PageResult<>(commentPage.getTotalElements(), commentPage.getTotalPages(), commentPage.getContent());
    }

    public Boolean exists(Integer id) {
        return commentRepository.existsById(id);
    }

    public List<Comment> findByMatterId(Integer matterId, Boolean show) {
        List<Comment> commentList = commentRepository.findCommentsByMatterIdAndIsShow(matterId, show);
        if (commentList.size() > 0) {
            commentList.forEach(comment -> {
                        comment.setUser(userService.findUserById(comment.getUserId()));
                        comment.setReplyList(replyService.findByComId(comment.getCommentId()));
                    }
            );
        }
        return commentList;
    }

    public JsonData updateShow(Integer id, Boolean type) {
        if (commentRepository.existsById(id)) {
            Comment comment = commentRepository.findById(id).get();
            comment.setIsShow(type);
            commentRepository.save(comment);
            return JsonData.buildSuccess("审批通过");
        }
        return JsonData.buildError("不存在该留言");
    }

    public PageResult<Comment> findByPage(Integer page, Integer size, String type){
        Specification<Comment> spec = new Specification<Comment>() {
            @Override
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                //根据属性名获取查询对象的属性
                list.add(criteriaBuilder.notEqual(root.get("matterId"), 0));
                if (type != null) {
                    list.add(criteriaBuilder.equal(root.get("isShow"), "1".equals(type)));
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        Page<Comment> commentPage = commentRepository.findAll(spec, PageRequest.of(page, size));
        commentPage.getContent().forEach(x -> {
            x.setTitle(matterService.byId(x.getMatterId()).getTitle());
            x.setUser(userService.findUserById(x.getUserId()));
        });
        return new PageResult<>(commentPage.getTotalElements(), commentPage.getTotalPages(), commentPage.getContent());
    }
}
