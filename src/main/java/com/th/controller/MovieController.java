package com.th.controller;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.th.bean.Movie;
import com.th.bean.MovieList;
import com.th.bean.vo.RateMessage;
import com.th.bean.vo.SelectStatusMessage;
import com.th.mapper.MovieListMapper;
import com.th.service.MovieListService;
import com.th.service.MovieRatingService;
import com.th.service.MovieService;
import com.th.service.RedisTemplateService;
import com.th.utils.CSVUtil;
import com.th.utils.ReturnObject;
import com.th.utils.MovieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


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

    @Autowired
    private MovieListService movieListService;

    @PostMapping("getMovieByPages")
    public String getMovieByPages(@RequestParam("currentPage") int currentPage, @RequestParam("size") int size) {
        System.out.println("getMovieByPages");
        List<RateMessage> rateMessages = MovieUtil.getRateMessage(redisTemplateService, movieRatingService);
        IPage<Movie> movieIPage = movieService.getMovieByPage(currentPage, size);
        MovieUtil.addRateToMovie(movieIPage.getRecords(), rateMessages);
        return JSONObject.toJSONString(new ReturnObject(movieIPage));
    }

    @PostMapping("getCurrentRatePeople")
    public String getCurrentRatePeople() {
        return JSONObject.toJSONString(new ReturnObject(movieService.getCurrentRatedPeople()));
    }

    @PostMapping("getMovieBySelectStatus")
    public String getMovieBySelectStatus(@RequestBody SelectStatusMessage statusMessage) {
        System.out.println(statusMessage);
        List<RateMessage> rateMessages = MovieUtil.getRateMessage(redisTemplateService, movieRatingService);
        IPage<Movie> movieIPage = movieService.getMovieBySelectStatus(statusMessage);
        List<MovieList> movieLists = movieListService.list(new QueryWrapper<MovieList>().eq("user_id", statusMessage.getUserId()).eq("list_name", "我的最爱"));
        MovieUtil.addRateToMovie(movieIPage.getRecords(), rateMessages);
        MovieUtil.addListToMovie(movieIPage.getRecords(), movieLists);
        MovieUtil.addUserRateToMovie(movieIPage.getRecords(), statusMessage.getUserId(), movieRatingService);
        return JSONObject.toJSONString(new ReturnObject(movieIPage));
    }

    @PostMapping("getMovieById")
    public String getMovieById(@RequestParam("id") int id, @RequestParam("uid") int uid) {
        List<RateMessage> rateMessages = MovieUtil.getRateMessage(redisTemplateService, movieRatingService);
        List<MovieList> movieLists = movieListService.list(new QueryWrapper<MovieList>().eq("user_id", uid).eq("list_name", "我的最爱"));
        return JSONObject.toJSONString(new ReturnObject(
                MovieUtil.addUserRateToMovie(
                        MovieUtil.addRateToMovie(
                                MovieUtil.addListToMovie(movieService.getById(id), movieLists), rateMessages), uid, movieRatingService)));
    }

    @PostMapping("getMovieByRating")
    public String getMovieByRating(@RequestParam("currentPage") int currentPage, @RequestParam("size") int size, @RequestParam("id") int id) {
        System.out.println(currentPage + "|" + size + "}" + id);
        IPage<Movie> page = movieService.getMovieByRating(new Page(currentPage, size), id);
        List<MovieList> movieLists = movieListService.list(new QueryWrapper<MovieList>().eq("user_id", id).eq("list_name", "我的最爱"));
        MovieUtil.addRateToMovie(page.getRecords(), MovieUtil.getRateMessage(redisTemplateService, movieRatingService));
        MovieUtil.addListToMovie(page.getRecords(), movieLists);
        return JSONObject.toJSONString(new ReturnObject(page), SerializerFeature.WriteMapNullValue);//不写的话有NULL值被直接忽略
    }

    @PostMapping("getMovieByKeyWord")
    public String getMovieByKeyWord(@RequestParam("keyword") String keyword) {
        System.out.println(keyword);
        return JSONObject.toJSONString(new ReturnObject(movieService.getMovieByKeyWord(keyword)));
    }

    @PostMapping("getMovieByUserRecommend")
    public String getMovieByUserRecommend(@RequestParam("uid") int id) {
        if (!redisTemplateService.exists("movieData")) {
            System.out.println("Overwrite MovieData");
            CSVUtil.movieToCSV(movieRatingService);
            redisTemplateService.set("movieData", true, 1, TimeUnit.DAYS);
        }
        List<Movie> movies = movieService.getMovieByUserRecommend(id);
        List<MovieList> movieLists = movieListService.list(new QueryWrapper<MovieList>().eq("user_id", id).eq("list_name", "我的最爱"));
        MovieUtil.addRateToMovie(movies, MovieUtil.getRateMessage(redisTemplateService, movieRatingService));
        MovieUtil.addListToMovie(movies, movieLists);
        return JSONObject.toJSONString(new ReturnObject(movies));
    }
}

