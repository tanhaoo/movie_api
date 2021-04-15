package com.th.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.th.bean.Movie;
import com.th.bean.vo.SelectStatusMessage;
import com.th.mapper.MovieMapper;
import com.th.service.MovieService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.th.service.RedisTemplateService;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
public class MovieServiceImpl extends ServiceImpl<MovieMapper, Movie> implements MovieService {

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private RedisTemplateService redisTemplateService;

    public IPage<Movie> getMovieByPage(int currentPage, int size) {
        Page<Movie> page = new Page<>(currentPage, size);
        QueryWrapper<Movie> queryWrapper = new QueryWrapper<>();
        return movieMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Integer getCurrentRatedPeople() {
        Integer ratePerson = 0;
        if (redisTemplateService.exists("rate_people")) {
            System.out.println("通过Redis获取getCurrentRatedPeople");
            return redisTemplateService.get("rate_people", Integer.class);
        } else {
            System.out.println("通过MySQL获取getCurrentRatedPeople");
            ratePerson = movieMapper.getRatePerson();
            //设置一个星期失效一次
            redisTemplateService.set("rate_people", ratePerson, 7, TimeUnit.DAYS);
        }
        return ratePerson;
    }

    @Override
    public IPage<Movie> getMovieBySelectStatus(SelectStatusMessage statusMessage) {
        //类型筛选
        String sqlGenreStatus = "";
        if (statusMessage.getType().length != 0) {
            sqlGenreStatus = "and m.genre like '%";
            //Comedy%Romance'
            for (String s : statusMessage.getType()) {
                sqlGenreStatus += (s + "%");
            }
            sqlGenreStatus += "'";
        }

        //评分筛选
        String sqlRating = "";
        String sqlRatingWhereStatus = "";
        String sqlRatingMaxStatus = "";
        if (statusMessage.getRating().length != 0) {
            sqlRating = " ,(select a.movie_id,score/vote as score from (select movie_id,sum(rating) as score from movie_rating GROUP BY movie_id)as a," +
                    "(select movie_id,count(*) as vote from movie_rating GROUP BY movie_id) as b " +
                    "where a.movie_id=b.movie_id)as c ";
            sqlRatingWhereStatus = " and m.id=c.movie_id ";
            sqlRatingMaxStatus = "and " + statusMessage.getRating()[0] + "<=c.score and c.score<=" + statusMessage.getRating()[1] + " ";
        }

        //时长筛选
        String sqlTimeStatus = "";
        if (statusMessage.getTime().length != 0) {
            sqlTimeStatus = " and " + statusMessage.getTime()[0] + "<=m.timer and m.timer<=" + statusMessage.getTime()[1] + " ";
        }

        //票数筛选
        String sqlVoteStatus = "";
        if (statusMessage.getVote() != 0) {
            sqlVoteStatus = " and b.vote<=" + statusMessage.getVote() + " ";
        }

        String sqlByHotUp = "select m.* from movie as m,(select movie_id,count(*) as vote from movie_rating group by movie_id ) as b" + sqlRating + " where m.id=b.movie_id "
                + sqlRatingWhereStatus + sqlRatingMaxStatus + sqlGenreStatus + sqlTimeStatus + sqlVoteStatus
                + " order by vote,m.id";
        String sqlByHotDrops = "select m.* from movie as m,(select movie_id,count(*) as vote from movie_rating group by movie_id ) as b" + sqlRating + " where m.id=b.movie_id "
                + sqlRatingWhereStatus + sqlRatingMaxStatus + sqlGenreStatus + sqlTimeStatus + sqlVoteStatus
                + " order by vote desc,m.id";

        String sqlByRateUp = "select m.* from (select a.movie_id,score/vote as score,vote from " +
                " (select movie_id,sum(rating) as score from movie_rating GROUP BY movie_id)as a," +
                " (select movie_id,count(*) as vote from movie_rating GROUP BY movie_id) as b where a.movie_id=b.movie_id )as b,movie as m" +
                sqlRating +
                " where b.movie_id=m.id " + sqlRatingWhereStatus + sqlRatingMaxStatus + sqlGenreStatus + sqlTimeStatus + sqlVoteStatus + " order by b.score ";
        String sqlByRateDrops = (sqlByRateUp + " desc");

        String sqlByTimeUp = "select m.* from movie as m " + sqlRating +
                " where m.id=m.id " +
                sqlRatingWhereStatus + sqlRatingMaxStatus + sqlGenreStatus + sqlTimeStatus + sqlVoteStatus + "order by timer";
        String sqlByTimeDrops = (sqlByTimeUp + " desc");

        String sqlByNameUp = "select m.* from movie as m " + sqlRating +
                " where m.id=m.id " + sqlRatingWhereStatus + sqlRatingMaxStatus + sqlGenreStatus + sqlTimeStatus + sqlVoteStatus + " order by m.movie_name";
        String sqlByNameDrops = (sqlByNameUp + " desc");

        Page<Movie> page = new Page<>(statusMessage.getCurrentPage(), statusMessage.getSize());
        System.out.println(sqlByRateUp);
        switch (statusMessage.getResultSort()) {
            case "hotDrops":
                return movieMapper.getMovieBySelectStatus(page, sqlByHotDrops);
            case "hotUp":
                return movieMapper.getMovieBySelectStatus(page, sqlByHotUp);
            case "rateDrops":
                return movieMapper.getMovieBySelectStatus(page, sqlByRateDrops);
            case "rateUp":
                return movieMapper.getMovieBySelectStatus(page, sqlByRateUp);
            case "timeDrops":
                return movieMapper.getMovieBySelectStatus(page, sqlByTimeDrops);
            case "timeUp":
                return movieMapper.getMovieBySelectStatus(page, sqlByTimeUp);
            case "nameDrops":
                return movieMapper.getMovieBySelectStatus(page, sqlByNameDrops);
            case "nameUp":
                return movieMapper.getMovieBySelectStatus(page, sqlByNameUp);
        }
        return null;
    }

    @Override
    public IPage<Movie> getMovieByRating(IPage page, int id) {
        return movieMapper.getMovieByRating(page, id);
    }

    @Override
    public List<Movie> getMovieByKeyWord(String keyword) {
        return movieMapper.getMovieByKeyWord(keyword);
    }

    @Override
    public List<Movie> getMovieByUserRecommend(int uid) {
        File file = new File("D:\\Data\\movie.csv");

        DataModel dataModel = null;
        List<Movie> movies = new ArrayList<>();
        try {
            dataModel = new FileDataModel(file);
            ItemSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
            Recommender recommender = new GenericItemBasedRecommender(dataModel, similarity);
            List<RecommendedItem> list = recommender.recommend(uid, 10);
            System.out.println(list);
            list.forEach(e -> {
                movies.add(movieMapper.selectById(e.getItemID()));
            });
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (TasteException e) {
            e.printStackTrace();
        }
        return movies;
    }
}
