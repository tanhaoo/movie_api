package com.th.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.th.bean.Movie;
import com.th.bean.MovieList;
import com.th.bean.vo.UserMovieListCount;
import com.th.mapper.MovieListMapper;
import com.th.service.MovieListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author TanHaooo
 * @date 2021/4/9 0:53
 */
@Service
public class MovieListServiceImpl extends ServiceImpl<MovieListMapper, MovieList> implements MovieListService {

    @Autowired
    private MovieListMapper movieListMapper;

    @Override
    public String InsertDelMovieList(MovieList movieList) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id", movieList.getUserId());
        wrapper.eq("movie_id", movieList.getMovieId());
        wrapper.eq("list_name", movieList.getListName());
        MovieList movie = movieListMapper.selectOne(wrapper);
        String result = "";
        if (movie == null) {
            movieListMapper.insert(movieList);
            return "已添加至" + movieList.getListName();
        } else {
            movieListMapper.delete(wrapper);
            return "已从" + movieList.getListName() + "移除";
        }
    }

    @Override
    public String InsertMovieLists(MovieList[] movieLists) {
        if (movieListMapper.selectList(new QueryWrapper<MovieList>().eq("list_name", movieLists[0].getListName())).size() != 0)
            return "列表 " + movieLists[0].getListName() + " 已存在";
        for (MovieList movieList : movieLists) {
            movieListMapper.insert(movieList);
        }
        return "已成功创建收藏列表 " + movieLists[0].getListName();
    }

    @Override
    public List<UserMovieListCount> getUserMovieListCount(int uid) {
        return movieListMapper.getUserMovieListCount(uid);
    }

    @Override
    public List<Movie> getMovieListByUserId(MovieList movieList) {
        return movieListMapper.getMovieListByUserId(movieList);
    }
}
