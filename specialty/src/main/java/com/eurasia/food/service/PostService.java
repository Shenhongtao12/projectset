package com.eurasia.food.service;

import com.eurasia.food.repository.PostRepository;
import com.eurasia.food.utils.JsonData;
import com.eurasia.food.utils.PageResult;
import com.eurasia.food.entity.Comment;
import com.eurasia.food.entity.Post;
import com.eurasia.food.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aaron
 * @date 2020/5/17 - 19:11
 **/
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    public PageResult<Post> init(){
        Page<Post> page = postRepository.findAll(PageRequest.of(0, 30, Sort.by(Sort.Direction.DESC, "createTime")));
        for (Post post : page) {
            String[] split = post.getImagesUrl().split(",");
            post.setImagesUrl(split[0]);
        }
        return new PageResult<>(page.getTotalElements(), page.getTotalPages(), page.getContent());
    }

    public JsonData save(Post post) {
        if (StringUtils.isEmpty(post.getUserId())){
            return JsonData.buildError("数据错误，未关联用户");
        }
        post.setNumber(0);
        post.setViews(0);
        post.setCreateTime(DateUtils.dateToString());
        postRepository.save(post);
        return JsonData.buildSuccess("成功");
    }
    //更新浏览量
    public void updateViews(Integer postId){
        Post post = postRepository.findById(postId).get();
        post.setViews(post.getViews() + 1);
        postRepository.save(post);
    }

    //暂时不允许更新
    public JsonData update(Post post) {
        Post result = postRepository.findById(post.getId()).get();
        if (StringUtils.isEmpty(result)){
            return JsonData.buildError("数据错误，不存在该postId");
        }
        postRepository.save(post);
        return JsonData.buildSuccess("更新成功");
    }

    @Transactional
    public JsonData delete(Integer id) {
        postRepository.deleteById(id);
        return JsonData.buildSuccess("删除成功");
    }

    public Post findById(Integer postId, Integer userId) {
        Post post = postRepository.findById(postId).get();
        post.setViews(post.getViews() + 1);
        postRepository.save(post);
        List<Comment> commentList = commentService.findByPostId(postId, userId, "createTime");
        post.setCommentList(commentList);
        post.setUser(userService.findUserById(post.getUserId()));
        post.setCommentNum(commentList.size());
        return post;
    }

    public Post findUserIdByPostId(Integer postId){
        return postRepository.findUserIdById(postId);
    }

    public Post findPostById(Integer postId) {
        return postRepository.findById(postId).get();
    }

    //根据userId查找post
    public List<Post> findByUserId(Integer userId){
        return postRepository.findByUserId(userId);
    }

    public PageResult<Post> findByClassify(Integer classifyId,String searchName, Integer page, Integer rows) {
        //自定义查询条件  匿名内部类
        Specification<Post> spec = new Specification<Post>() {
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                //根据属性名获取查询对象的属性
                //Path<Reply> path = root.get("nameId");
                //相当于 where receiverName = "Veggie", CriteriaBuilder接口中还有很多查询条件，建议看源码
                //Predicate equal = criteriaBuilder.equal(path, userId);
                if(classifyId != null){
                    list.add(criteriaBuilder.equal(root.get("classifyId"), classifyId));
                }
                if (!StringUtils.isEmpty(searchName)){
                    list.add(criteriaBuilder.like(root.get("title"), "%" + searchName + "%"));
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        Page<Post> postPage = postRepository.findAll(spec, PageRequest.of(page, rows));
        for (Post post : postPage) {
            post.setUser(userService.findUserById(post.getUserId()));
            post.setCommentNum(commentService.countByPostId(post.getId()));
        }
        return new PageResult<>(postPage.getTotalElements(), postPage.getTotalPages(), postPage.getContent());
    }
}