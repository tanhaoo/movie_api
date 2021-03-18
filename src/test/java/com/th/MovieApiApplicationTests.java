package com.th;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.th.bean.Movie;
import com.th.bean.User;
import com.th.mapper.UserMapper;
import com.th.service.MovieService;
import com.th.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class MovieApiApplicationTests {
    @Autowired
    private UserService userService;

    @Autowired
    private MovieService movieService;

    @Test
    void contextLoads() {
    }

    @Test
    void testSelect() {
        BaseMapper<User> users = userService.getBaseMapper();
        User login = userService.login("1", "1");
        System.out.println(login);
        System.out.println(users.selectList(null));
    }

    @Test
    void testSelectMovieByPage() {
        IPage page = movieService.getMovieByPage(1, 30);
        List<Movie> movies=page.getRecords();
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        System.out.println(page.getTotal());
    }

}
