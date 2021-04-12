package com.th;

import com.th.bean.MovieList;
import com.th.mapper.MovieListMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

/**
 * @author TanHaooo
 * @date 2021/4/7 19:11
 */
@SpringBootTest
public class MovieListTest {

    @Autowired
    private MovieListMapper movieListMapper;

    @Test
    public void testInsert() {
        MovieList movieList = new MovieList();
        movieList.setUserId(2);
        movieList.setMovieId(1);
        movieList.setListName("我的最爱");
        movieListMapper.insert(movieList);
    }
}
