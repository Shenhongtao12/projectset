package com.sht.shoesboot.controller;

import com.sht.shoesboot.entity.Favorite;
import com.sht.shoesboot.service.FavoriteService;
import com.sht.shoesboot.utils.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Aaron
 * @date 2020/12/24 23:31
 */
@RestController
@RequestMapping("api/favorite")
public class FavoriteController extends BaseController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<RestResponse> collect(@RequestBody Favorite favorite) {
        favoriteService.collect(favorite);
        return ResponseEntity.ok().body(SUCCESS("成功"));
    }

    @GetMapping
    public ResponseEntity<RestResponse> queryFavorite(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                      @RequestParam(name = "size", defaultValue = "20")Integer size){
        return ResponseEntity.ok().body(SUCCESS(favoriteService.queryFavorite(userId, page, size)));
    }

    @DeleteMapping("ids")
    public ResponseEntity<RestResponse> batchDelete(@RequestParam(name = "ids")List<Integer> ids) {
        favoriteService.batchDelete(ids);
        return ResponseEntity.ok().body(SUCCESS("成功"));
    }

    @DeleteMapping
    public ResponseEntity<RestResponse> delete(@Valid @NotNull(message = "userId not is null") @RequestParam(name = "userId")Integer userId,
                                               @Valid @NotNull(message = "goodsId not is null") @RequestParam(name = "goodsId") Integer goodsId) {
        favoriteService.delete(userId, goodsId);
        return ResponseEntity.ok().body(SUCCESS("删除成功"));
    }

}
