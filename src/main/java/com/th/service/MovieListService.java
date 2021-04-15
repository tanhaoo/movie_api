package com.th.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.th.bean.Movie;
import com.th.bean.MovieList;
import com.th.bean.vo.UserMovieListCount;

import java.util.List;

/**
 * @author TanHaooo
 * @date 2021/4/9 0:52
 */
public interface MovieListService extends IService<MovieList> {
    String InsertDelMovieList(MovieList movieList);

    String InsertMovieLists(MovieList[] movieLists);

    List<UserMovieListCount> getUserMovieListCount(int uid);

    List<Movie> getMovieListByUserId(MovieList movieList);
}
