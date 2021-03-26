package com.th.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.th.bean.Movie;
import com.th.mapper.MovieMapper;
import com.th.service.MovieService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.th.service.RedisTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author TanHaooo
 * @since 2021-03-18
 */
@Service
public class MovieServiceImpl extends ServiceImpl<MovieMapper, Movie> implements MovieService {

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private RedisTemplateService redisTemplateService;

    public IPage<Movie> getMovieByPage(int currentPage, int size) {
        Page<Movie> page = new Page<>(currentPage, size);
        QueryWrapper<Movie> queryWrapper = new QueryWrapper<>();
        return movieMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Integer getCurrentRatedPeople() {
        Integer ratePerson = 0;
        if (redisTemplateService.exists("rate_people"))
        {
            System.out.println("通过Redis调用getCurrentRatedPeople");
            return redisTemplateService.get("rate_people", Integer.class);
        }
        else {
            System.out.println("通过MySQL调用getCurrentRatedPeople");
            ratePerson = movieMapper.getRatePerson();
            //设置一个星期失效一次
            redisTemplateService.set("rate_people", ratePerson, 7, TimeUnit.DAYS);
        }
        return ratePerson;
    }
}
