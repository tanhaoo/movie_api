package com.th.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.th.bean.Movie;
import com.baomidou.mybatisplus.extension.service.IService;
import com.th.bean.vo.SelectStatusMessage;

import java.util.List;

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

    IPage<Movie> getMovieBySelectStatus(SelectStatusMessage statusMessage);

    IPage<Movie> getMovieByRating(IPage page, int id);

    List<Movie> getMovieByKeyWord(String keyword);

    List<Movie> getMovieByUserRecommend(int uid);
}
