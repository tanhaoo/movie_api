package com.th.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.th.bean.Movie;
import com.th.service.MovieService;
import com.th.utils.retunJson.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
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

    @PostMapping("getMovieByPages")

    public String getMovieByPages(@RequestParam("currentPage") int currentPage, @RequestParam("size") int size) {
        System.out.println("getMovieByPages");
        return JSONObject.toJSONString(new ReturnObject(movieService.getMovieByPage(currentPage, size)));
    }
}

