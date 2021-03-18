package com.th.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.th.bean.Movie;
import com.th.mapper.MovieMapper;
import com.th.service.MovieService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public IPage<Movie> getMovieByPage(int currentPage, int size) {
        Page<Movie> page = new Page<>(currentPage, size);
        QueryWrapper<Movie> queryWrapper = new QueryWrapper<>();
        return movieMapper.selectPage(page, queryWrapper);
    }
}
