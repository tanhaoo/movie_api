package com.th.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.th.bean.Movie;
import com.th.bean.MovieList;
import com.th.bean.vo.RateMessage;
import com.th.bean.vo.UserMovieListCount;
import com.th.service.MovieListService;
import com.th.service.MovieRatingService;
import com.th.service.MovieService;
import com.th.service.RedisTemplateService;
import com.th.utils.MovieUtil;
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
    @Autowired
    private MovieService movieService;
    @Autowired
    private RedisTemplateService redisTemplateService;
    @Autowired
    private MovieRatingService movieRatingService;


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

    @PostMapping("getMovieListByUserId")
    public String getMovieListByUserId(@RequestBody MovieList movieList) {
        System.out.println("getMovieListByUserId" + movieList);
        List<Movie> movies = movieListService.getMovieListByUserId(movieList);
        List<RateMessage> rateMessages = MovieUtil.getRateMessage(redisTemplateService, movieRatingService);
        List<MovieList> movieLists = movieListService.list(new QueryWrapper<MovieList>().eq("user_id", movieList.getUserId()).eq("list_name", "????????????"));
        MovieUtil.addListToMovie(movies, movieLists);
        MovieUtil.addRateToMovie(movies, rateMessages);
        return JSON.toJSONString(new ReturnObject(movies));
    }

    @PostMapping("updateMovieList")
    public String updateMovieList(@RequestBody MovieList[] movieList) {
        System.out.println(Arrays.toString(movieList));
        return JSONObject.toJSONString(new ReturnObject(
                movieListService.update(movieList[1], new QueryWrapper<MovieList>()
                        .eq("user_id", movieList[0].getUserId()).eq("list_name", movieList[0].getListName()))
                        ? "?????????" + movieList[1].getListName() + "???????????????"
                        : "?????????" + movieList[1].getListName() + "????????????"));
    }

    @PostMapping("deleteMovieList")
    public String deleteMovieList(@RequestBody MovieList movieList) {
        return JSONObject.toJSONString(new ReturnObject(movieListService.remove(new QueryWrapper<MovieList>()
                .eq("user_id", movieList.getUserId())
                .eq("list_name", movieList.getListName()))
                ? "??????" + movieList.getListName() + "?????????"
                : "??????" + movieList.getListName() + "????????????"));
    }

    @PostMapping("getMovieListNameByUserId")
    public String getMovieListNameByUserId(@RequestBody MovieList movieList) {

        return JSONObject.toJSONString(new ReturnObject(movieListService.list(new QueryWrapper<MovieList>()
                .select("list_name")
                .eq("user_id", movieList.getUserId())
                .ne("list_name", "????????????")
                .groupBy("list_name"))));
    }

    @PostMapping("insertMovieToList")
    public String insertMovieToList(@RequestBody MovieList movieList) {
        return JSONObject.toJSONString(new ReturnObject(
                movieListService.getOne(new QueryWrapper<MovieList>()
                        .eq("user_id", movieList.getUserId())
                        .eq("list_name", movieList.getListName())
                        .eq("movie_id", movieList.getMovieId())) == null ?
                        movieListService.save(movieList)
                                ? "??????????????????'" + movieList.getListName() + "'"
                                : "'" + movieList.getListName() + "'????????????"
                        : "'" + movieList.getListName() + "'??????????????????"
        ));
    }

    @PostMapping("removeMovieFromList")
    public String removeMovieFromList(@RequestBody MovieList movie) {
        return JSONObject.toJSONString(new ReturnObject(movieListService.remove(
                new QueryWrapper<MovieList>()
                        .eq("user_id", movie.getUserId())
                        .eq("list_name", movie.getListName())
                        .eq("movie_id", movie.getMovieId())
        ) ? "???????????????????????????" : "????????????"
        ));
    }
}
