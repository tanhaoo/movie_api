package com.th;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.th.bean.Movie;
import com.th.bean.User;
import com.th.bean.vo.RateMessage;
import com.th.mapper.MovieMapper;
import com.th.mapper.MovieRatingMapper;
import com.th.mapper.UserMapper;
import com.th.service.MovieService;
import com.th.service.RedisTemplateService;
import com.th.service.UserService;
import com.th.utils.MathUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


@SpringBootTest
class MovieApiApplicationTests {
    @Autowired
    private UserService userService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private RedisTemplateService redisTemplateService;

    @Autowired
    private MovieRatingMapper movieRatingMapper;

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
        List<Movie> movies = page.getRecords();
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        System.out.println(page.getTotal());
    }

    @Test
    void testGetMovieType() {
        long current = 1;
        List<String> types = new ArrayList<>();
        List<String> tempType = new ArrayList<>();

        Page<Movie> pages;
        QueryWrapper<Movie> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("genre");
        IPage page;
        while (current <= 9729) {
            pages = new Page<>(current, 100);
            current++;
            page = movieMapper.selectPage(pages, queryWrapper);
            List<Movie> reslut = page.getRecords();
            for (Movie movie : reslut) {
                String type = movie.getGenre();
                tempType = (Arrays.asList(type.split("\\|")));
                for (String s : tempType) {
                    if (!types.contains(s)) types.add(s);
                }
            }
        }
        System.out.println("types" + types);
    }

    @Test
    public void testRedisSet() {
        Movie movie = movieMapper.selectById(1);
        System.out.println(movie);
        if (redisTemplateService.exists("test1")) {
            redisTemplateService.deleteKey("test1");
        }
        boolean flag = redisTemplateService.set("test1", movie, 7, TimeUnit.SECONDS);
        System.out.println(flag);
        Movie o = redisTemplateService.get("test1", Movie.class);
        System.out.println(o);
    }

    @Test
    public void testRedisGet() {
        int ratePerson;
        if (redisTemplateService.exists("rate_people"))
            System.out.println("Redis" + redisTemplateService.get("rate_people", Integer.class));
        else {
            ratePerson = movieMapper.getRatePerson();
            redisTemplateService.set("rate_people", ratePerson, 60, TimeUnit.SECONDS);
            System.out.println("MySql" + ratePerson);
        }
    }

    @Test
    public void testMySql() {
        System.out.println(movieMapper.getRatePerson());
    }

    @Test
    public void testGetRateMessage() {
        List<RateMessage> rateMessages = movieRatingMapper.getRateMessage();
        System.out.println(rateMessages);
        for (RateMessage rateMessage : rateMessages) {
            rateMessage.setRate(MathUtil.round(rateMessage.getRate(), 1, BigDecimal.ROUND_HALF_UP));
            System.out.println(rateMessage.getMovieId() + "||" + rateMessage.getRate());
        }
        redisTemplateService.set("rate_message", rateMessages, 7, TimeUnit.DAYS);
        System.out.println(MathUtil.round(12.742, 1, BigDecimal.ROUND_HALF_UP));
    }

    @Test
    public void testRateMessageRedis() {
        List<RateMessage> rateMessages = new ArrayList<>();
        if (redisTemplateService.exists("rate_message")) {
            // rateMessages = (List<RateMessage>) redisTemplateService.get("rate_message", List.class);
            rateMessages = redisTemplateService.getList("rate_message", RateMessage.class);
        }
        System.out.println(rateMessages);
    }
}
