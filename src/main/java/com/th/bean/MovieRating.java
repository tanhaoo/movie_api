package com.th.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author TanHaooo
 * @since 2021-03-18
 */
public class MovieRating implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer userId;

    private Integer movieId;

    private Float rating;

    private String timestamp;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public MovieRating() {
    }

    public MovieRating(Integer userId, Integer movieId, Float rating, String timestamp) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "MovieRating{" +
        "userId=" + userId +
        ", movieId=" + movieId +
        ", rating=" + rating +
        ", timestamp=" + timestamp +
        "}";
    }
}
