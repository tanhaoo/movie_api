package com.th.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.th.bean.Movie;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author TanHaooo
 * @since 2021-03-18
 */
@Component
public interface MovieMapper extends BaseMapper<Movie> {
    Integer getRatePerson();

    IPage<Movie> getMovieBySelectStatus(IPage page, String sql);

    IPage<Movie> getMovieByRating(IPage page, int id);

    List<Movie> getMovieByKeyWord(String keyword);
}
