package com.th.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.th.bean.Movie;
import com.th.bean.MovieRating;
import com.th.service.MovieRatingService;
import com.th.utils.ReturnObject;
import com.th.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author TanHaooo
 * @since 2021-03-18
 */
@RestController
@RequestMapping("/movieRating")
public class MovieRatingController {
    @Autowired
    private MovieRatingService movieRatingService;

    @PostMapping("updateRatingByUserId")
    public String updateRatingByUserId(@RequestParam("uid") int uid, @RequestParam("mid") int mid, @RequestParam("rating") float rating) {
        if (movieRatingService.getOne(new QueryWrapper<MovieRating>()
                .eq("user_id", uid).eq("movie_id", mid)) == null) {
            movieRatingService.save(new MovieRating(uid, mid, rating,
                    TimeUtil.TimeFormatToTimeStamp(System.currentTimeMillis())));
        } else {
            QueryWrapper wrapper = new QueryWrapper();
            MovieRating mr = new MovieRating();
            mr.setRating(rating);
            wrapper.eq("user_id", uid);
            wrapper.eq("movie_id", mid);
            movieRatingService.update(mr, wrapper);
        }
        return JSON.toJSONString(new ReturnObject(true));
    }

    @PostMapping("delRatingByUserId")
    public String delRatingByUserId(@RequestBody MovieRating movieRating) {
        System.out.println(movieRating);
        return JSON.toJSONString(new ReturnObject(
                movieRatingService.remove(new QueryWrapper<MovieRating>()
                        .eq("user_id", movieRating.getUserId())
                        .eq("movie_id", movieRating.getMovieId())) ? "删除评分成功" : "删除评分失败"
        ));
    }
}

