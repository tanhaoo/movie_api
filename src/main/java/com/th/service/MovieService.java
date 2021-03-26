package com.th.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.th.bean.Movie;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author TanHaooo
 * @since 2021-03-18
 */
public interface MovieService extends IService<Movie> {
    IPage<Movie> getMovieByPage(int currentPage, int size);

    Integer getCurrentRatedPeople();
}
