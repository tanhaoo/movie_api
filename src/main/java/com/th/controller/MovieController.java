package com.th.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.th.bean.Movie;
import com.th.bean.vo.RateMessage;
import com.th.service.MovieRatingService;
import com.th.service.MovieService;
import com.th.service.RedisTemplateService;
import com.th.utils.ReturnObject;
import com.th.utils.MovieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author TanHaooo
 * @since 2021-03-18
 */
@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private RedisTemplateService redisTemplateService;

    @Autowired
    private MovieRatingService movieRatingService;

    @PostMapping("getMovieByPages")
    public String getMovieByPages(@RequestParam("currentPage") int currentPage, @RequestParam("size") int size) {
        System.out.println("getMovieByPages");
        List<RateMessage> rateMessages = new ArrayList<>();
        if (redisTemplateService.exists("rate_message")) {
            System.out.println("通过Redis获取getRateMessage");
            rateMessages = redisTemplateService.getList("rate_message", RateMessage.class);
        } else {
            System.out.println("通过MySQL获取getRateMessage");
            rateMessages = movieRatingService.getRateMessage();
        }
        IPage<Movie> movieIPage = movieService.getMovieByPage(currentPage, size);
        MovieUtil.addRateToMovie(movieIPage.getRecords(), rateMessages);
        return JSONObject.toJSONString(new ReturnObject(movieIPage));
    }

    @PostMapping("getCurrentRatePeople")
    public String getCurrentRatePeople() {
        return JSONObject.toJSONString(new ReturnObject(movieService.getCurrentRatedPeople()));
    }
}

