package com.th.utils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.th.bean.Movie;
import com.th.bean.vo.RateMessage;

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
                    .filter(rateMessage -> Objects.equals(movie.getId(),rateMessage.getMovieId()))
                    .forEach(rateMessage -> {
                        movie.setRating(rateMessage.getRate());
                    });
            return movie;
        }).collect(Collectors.toList());
    }
}
