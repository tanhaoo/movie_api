package com.th.bean.vo;

import com.th.bean.Movie;
import com.th.bean.MovieRating;
import lombok.Data;

/**
 * @author TanHaooo
 * @date 2021/4/6 20:55
 */
@Data
public class UserMovieRating {
    private Movie movie;
    private MovieRating movieRating;
}
