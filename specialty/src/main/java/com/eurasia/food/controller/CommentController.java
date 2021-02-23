package com.eurasia.food.controller;

import com.eurasia.food.entity.Comment;
import com.eurasia.food.service.CommentService;
import com.eurasia.food.utils.JsonData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"api/comment"})
@Api(tags = "留言服务")
public class CommentController extends BaseController{

    @Autowired
    private CommentService commentService;

    @PostMapping()
    public ResponseEntity<JsonData> save(@RequestBody Comment comment){
        comment.setUserId(userId);
        return ResponseEntity.ok(this.commentService.save(comment));
    }

    @DeleteMapping()
    public ResponseEntity<JsonData> delete(@RequestParam(name = "id") Integer id) {
        return ResponseEntity.ok(this.commentService.delete(id));
    }

    @GetMapping()
    public ResponseEntity<JsonData> findOneComment(@RequestParam(name = "id") Integer id){
        return ResponseEntity.ok(JsonData.buildSuccess(commentService.findOneComment(id, userId), ""));
    }

    @GetMapping("findBySort")
    public ResponseEntity<JsonData> findBySort(@RequestParam(name = "postId") Integer postId,
                                               @RequestParam(name = "sortName") String sortName){
        return ResponseEntity.ok(JsonData.buildSuccess(commentService.findByPostId(postId, userId, sortName),""));
    }

    @GetMapping("findMessagePage")
    public ResponseEntity<JsonData> findMessagePage(@RequestParam(name = "page")Integer page,
                                                    @RequestParam(name = "rows") Integer rows) {
        return ResponseEntity.ok(JsonData.buildSuccess(commentService.findMessagePage(page, rows), ""));
    }

    @PutMapping
    public ResponseEntity<JsonData> isShow(@RequestParam(name = "id") Integer id,
                                           @RequestParam(name = "type") Boolean type) {
        return ResponseEntity.ok(commentService.updateShow(id, type));
    }

    @GetMapping("approval")
    public ResponseEntity<JsonData> approval(@RequestParam(name = "page", defaultValue = "0")Integer page,
                                             @RequestParam(name = "rows", defaultValue = "10") Integer rows,
                                             @ApiParam(name = "type", value = "不传该参数就查询所有, '1': isShow=true  '2':isShow=false") @RequestParam(name = "type", required = false) String type) {
        return ResponseEntity.ok(JsonData.buildSuccess(commentService.findByPage(page, rows, type), ""));
    }
}
