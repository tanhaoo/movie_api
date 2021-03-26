package com.th.service;

import com.th.bean.MovieRating;
import com.baomidou.mybatisplus.extension.service.IService;
import com.th.bean.vo.RateMessage;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author TanHaooo
 * @since 2021-03-18
 */
public interface MovieRatingService extends IService<MovieRating> {
    List<RateMessage> getRateMessage();
}
