package com.th.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csvreader.CsvWriter;
import com.th.bean.MovieRating;
import com.th.mapper.MovieMapper;
import com.th.service.MovieRatingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author TanHaooo
 * @date 2021/4/13 23:47
 */
public class CSVUtil {

    public static void movieToCSV(MovieRatingService movieRatingService) {
        String path = "D:\\Data\\movie.csv";
        try {
            CsvWriter csvWriter = new CsvWriter(path, ',', Charset.forName("GBK"));
//                String[] headers = {"user_id", "movie_id", "rating", "timestamp"};
//                csvWriter.writeRecord(headers);
            List<MovieRating> movies = movieRatingService.list();
            for (MovieRating movie : movies) {
                String[] content = {String.valueOf(movie.getUserId()), String.valueOf(movie.getMovieId()), String.valueOf(movie.getRating()), movie.getTimestamp()};
                csvWriter.writeRecord(content);
            }
            csvWriter.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
