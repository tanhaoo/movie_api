package com.th.controller;


import com.alibaba.fastjson.JSONObject;
import com.th.service.MovieService;
import com.th.service.RedisTemplateService;
import com.th.utils.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("getMovieByPages")
    public String getMovieByPages(@RequestParam("currentPage") int currentPage, @RequestParam("size") int size) {
        System.out.println("getMovieByPages");
        if(redisTemplateService.exists("rate_message")){

        }
        return JSONObject.toJSONString(new ReturnObject(movieService.getMovieByPage(currentPage, size)));
    }

    @PostMapping("getCurrentRatePeople")
    public String getCurrentRatePeople() {
        return JSONObject.toJSONString(new ReturnObject(movieService.getCurrentRatedPeople()));
    }
}

