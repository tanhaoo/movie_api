package com.th.bean.vo;

import lombok.Data;

/**
 * @author TanHaooo
 * @date 2021/3/26 16:03
 */
@Data
public class RateMessage {
    private String movieId;
    private double rate;

    public RateMessage(String movieId, long rate) {
        this.movieId = movieId;
        this.rate = rate;
    }
}
