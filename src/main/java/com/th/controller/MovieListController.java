package com.th.controller;

import com.alibaba.fastjson.JSON;
import com.th.bean.MovieList;
import com.th.bean.vo.UserMovieListCount;
import com.th.service.MovieListService;
import com.th.utils.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author TanHaooo
 * @date 2021/4/9 1:30
 */
@RestController
@RequestMapping("movieList")
public class MovieListController {

    @Autowired
    private MovieListService movieListService;

    @PostMapping("InsertDelMovieList")
    public String InsertDelMovieList(@RequestBody MovieList movieList) {
        System.out.println(movieList);
        return JSON.toJSONString(new ReturnObject(movieListService.InsertDelMovieList(movieList)));
    }

    @PostMapping("InsertMovieLists")
    public String InsertMovieLists(@RequestBody MovieList[] movieLists) {
        System.out.println(Arrays.toString(movieLists));
        return JSON.toJSONString(new ReturnObject(movieListService.InsertMovieLists(movieLists)));
    }

    @PostMapping("getUserMovieListCount")
    public String getUserMovieListCount(int uid) {
        System.out.println("getUserMovieListCount " + uid);
        return JSON.toJSONString(new ReturnObject(movieListService.getUserMovieListCount(uid)));
    }

}
