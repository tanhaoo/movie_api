package com.th.service.impl;

import com.th.bean.MovieRating;
import com.th.bean.vo.RateMessage;
import com.th.mapper.MovieRatingMapper;
import com.th.service.MovieRatingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.th.utils.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
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
public class MovieRatingServiceImpl extends ServiceImpl<MovieRatingMapper, MovieRating> implements MovieRatingService {

    @Autowired
    private MovieRatingMapper movieRatingMapper;

    @Autowired
    RedisTemplateServiceImpl redisTemplateService;

    @Override
    public List<RateMessage> getRateMessage() {
        List<RateMessage> rateMessages = movieRatingMapper.getRateMessage();
        rateMessages.forEach(e->{
            e.setRate(MathUtil.roundTwo(e.getRate(), 1, BigDecimal.ROUND_HALF_UP));
        });
        redisTemplateService.set("rate_message", rateMessages, 7, TimeUnit.DAYS);
        return rateMessages;
    }


}
