package com.th.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.th.bean.Movie;
import com.th.bean.MovieList;
import com.th.bean.vo.UserMovieListCount;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author TanHaooo
 * @date 2021/4/7 19:08
 */
@Component
public interface MovieListMapper extends BaseMapper<MovieList> {
    List<UserMovieListCount> getUserMovieListCount(int uid);

    List<Movie> getMovieListByUserId(MovieList movieList);
}
