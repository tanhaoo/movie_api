package com.th.utils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.th.bean.Movie;
import com.th.bean.MovieList;
import com.th.bean.MovieRating;
import com.th.bean.vo.RateMessage;
import com.th.service.MovieRatingService;
import com.th.service.MovieService;
import com.th.service.RedisTemplateService;
import com.th.service.impl.RedisTemplateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author TanHaooo
 * @date 2021/3/30 17:15
 */
public class MovieUtil {


    public static void addRateToMovie(List<Movie> movieList, List<RateMessage> rateMessages) {
        movieList.stream().map(movie -> {
            rateMessages.stream()
                    .filter(rateMessage -> Objects.equals(movie.getId(), rateMessage.getMovieId()))
                    .forEach(rateMessage -> {
                        movie.setRating(rateMessage.getRate());
                    });
            return movie;
        }).collect(Collectors.toList());
    }

    public static void addListToMovie(List<Movie> movies, List<MovieList> movieLists) {
        movies.stream().map(movie -> {
            movieLists.stream().filter(movieList -> Objects.equals(movie.getId(), movieList.getMovieId()))
                    .forEach(movieList -> {
                        movie.setMovieList(movieList);
                    });
            return movie;
        }).collect(Collectors.toList());
    }

    public static Movie addRateToMovie(Movie movie, List<RateMessage> rateMessages) {
        rateMessages.stream().filter(e ->
                Objects.equals(movie.getId(), e.getMovieId())
        ).forEach(e -> {
                    movie.setRating(e.getRate());
                }
        );
        return movie;
    }

//    public static void addMovieToMovieList(List<MovieList> movieLists, List<Movie> movies) {
//        movieLists.stream().map(movieList -> {
//            movies.stream().filter(movie -> Objects.equals(movie.getId(), movieList.getMovieId())).forEach(movie -> {
//                movie.setMovieList(movieList);
//            });
//            return movieList;
//        }).collect(Collectors.toList());
//    }

    public static List<RateMessage> getRateMessage(RedisTemplateService redisTemplateService, MovieRatingService movieRatingService) {
        if (redisTemplateService.exists("rate_message")) {
            System.out.println("通过Redis获取getRateMessage");
            return redisTemplateService.getList("rate_message", RateMessage.class);
        } else {
            System.out.println("通过MySQL获取getRateMessage");
            return movieRatingService.getRateMessage();
        }
    }

    public static void addUserRateToMovie(List<Movie> movies, int uid, MovieRatingService movieRatingService) {
        movies.stream().map(movie -> {
            movie.setMovieRating(movieRatingService.getOne(new QueryWrapper<MovieRating>()
                    .eq("user_id", uid)
                    .eq("movie_id", movie.getId())));
            return movie;
        }).collect(Collectors.toList());
    }
}
