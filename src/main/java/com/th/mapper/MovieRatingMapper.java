package com.th.mapper;

import com.th.bean.MovieRating;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.th.bean.vo.RateMessage;
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
public interface MovieRatingMapper extends BaseMapper<MovieRating> {

    List<RateMessage> getRateMessage();
}
