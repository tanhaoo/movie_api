package com.th;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csvreader.CsvWriter;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.th.bean.Movie;
import com.th.bean.MovieList;
import com.th.bean.MovieRating;
import com.th.bean.User;
import com.th.bean.vo.RateMessage;
import com.th.bean.vo.ResultSort;
import com.th.bean.vo.SelectStatusMessage;
import com.th.bean.vo.UserMovieListCount;
import com.th.mapper.MovieMapper;
import com.th.mapper.MovieRatingMapper;
import com.th.mapper.UserMapper;
import com.th.service.*;
import com.th.utils.MathUtil;
import com.th.utils.MovieUtil;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


@SpringBootTest
class MovieApiApplicationTests {
    @Autowired
    private UserService userService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private RedisTemplateService redisTemplateService;

    @Autowired
    private MovieRatingMapper movieRatingMapper;

    @Autowired
    private MovieRatingService movieRatingService;

    @Autowired
    private MovieListService movieListService;


    @Test
    void contextLoads() {

    }

    @Test
    void testSelect() {
        BaseMapper<User> users = userService.getBaseMapper();
        User login = userService.login("1", "1");
        System.out.println(login);
        System.out.println(users.selectList(null));
    }

    @Test
    void testSelectMovieByPage() {
        IPage page = movieService.getMovieByPage(1, 30);
        List<Movie> movies = page.getRecords();
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        System.out.println(page.getTotal());
    }

    @Test
    void testGetMovieType() {
        long current = 1;
        List<String> types = new ArrayList<>();
        List<String> tempType = new ArrayList<>();

        Page<Movie> pages;
        QueryWrapper<Movie> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("genre");
        IPage page;
        while (current <= 9729) {
            pages = new Page<>(current, 100);
            current++;
            page = movieMapper.selectPage(pages, queryWrapper);
            List<Movie> reslut = page.getRecords();
            for (Movie movie : reslut) {
                String type = movie.getGenre();
                tempType = (Arrays.asList(type.split("\\|")));
                for (String s : tempType) {
                    if (!types.contains(s)) types.add(s);
                }
            }
        }
        System.out.println("types" + types);
    }

    @Test
    public void testRedisSet() {
        Movie movie = movieMapper.selectById(1);
        System.out.println(movie);
        if (redisTemplateService.exists("test1")) {
            redisTemplateService.deleteKey("test1");
        }
        boolean flag = redisTemplateService.set("test1", movie, 7, TimeUnit.SECONDS);
        System.out.println(flag);
        Movie o = redisTemplateService.get("test1", Movie.class);
        System.out.println(o);
    }

    @Test
    public void testRedisGet() {
        int ratePerson;
        if (redisTemplateService.exists("rate_people"))
            System.out.println("Redis" + redisTemplateService.get("rate_people", Integer.class));
        else {
            ratePerson = movieMapper.getRatePerson();
            redisTemplateService.set("rate_people", ratePerson, 60, TimeUnit.SECONDS);
            System.out.println("MySql" + ratePerson);
        }
    }

    @Test
    public void testMySql() {
//        List<MovieList> movieLists = movieListService.list(new QueryWrapper<MovieList>().eq("user_id", 1));
//        System.out.println(movieLists);
//        IPage<Movie> page = movieService.getMovieByRating(new Page(1, 30), 1);
//        MovieUtil.addListToMovie(page.getRecords(), movieLists);
//        System.out.println("----------------");
//        System.out.println(page.getRecords());
//        MovieList movieList = new MovieList();
//        movieList.setListName("我的最1爱");
//        movieList.setMovieId(1);
//        movieList.setUserId(1);
//        System.out.println(movieListService.InsertDelMovieList(movieList));
        List<UserMovieListCount> movies = new ArrayList<>();
        movies = movieListService.getUserMovieListCount(1);
        System.out.println(movies);
    }

    @Test
    public void testGetRateMessage() {
        List<RateMessage> rateMessages = movieRatingMapper.getRateMessage();
        System.out.println(rateMessages);
        for (RateMessage rateMessage : rateMessages) {
            rateMessage.setRate(MathUtil.round(rateMessage.getRate(), 1, BigDecimal.ROUND_HALF_UP));
            System.out.println(rateMessage.getMovieId() + "||" + rateMessage.getRate());
        }
        redisTemplateService.set("rate_message", rateMessages, 7, TimeUnit.DAYS);
        System.out.println(MathUtil.round(12.742, 1, BigDecimal.ROUND_HALF_UP));
    }

    @Test
    public void testRateMessageRedis() {
        List<RateMessage> rateMessages = new ArrayList<>();
        if (redisTemplateService.exists("rate_message")) {
            // rateMessages = (List<RateMessage>) redisTemplateService.get("rate_message", List.class);
            rateMessages = redisTemplateService.getList("rate_message", RateMessage.class);
        }
        System.out.println(rateMessages);
    }

    @Test
    public void testMahout() throws IOException, TasteException {
//        MysqlDataSource dataSource = new MysqlDataSource();
//        dataSource.setServerName("localhost");
//        dataSource.setUser("root");
//        dataSource.setPassword("123456");
//        dataSource.setDatabaseName("moviesys");
//        DataModel jdbcDataModel = new MySQLJDBCDataModel(dataSource, "movie_rating", "user_id", "movie_id", "rating", "timestamp");
//        UserSimilarity similarity = new PearsonCorrelationSimilarity(jdbcDataModel);
//        UserNeighborhood neighborhood = new NearestNUserNeighborhood(10, similarity, jdbcDataModel);
//        Recommender recommender = new GenericUserBasedRecommender(jdbcDataModel, neighborhood, similarity);
//        LongPrimitiveIterator iterator = jdbcDataModel.getUserIDs();

        File file = new File("D:\\Data\\movie.csv");
        DataModel dataModel = new FileDataModel(file);
//        // 计算相似度，相似度算法有很多种，欧几里得、皮尔逊等等。
//        UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
//        // 计算最近邻域，邻居有两种算法，基于固定数量的邻居和基于相似度的邻居，这里使用基于固定数量的邻居
//        UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(10, similarity, dataModel);
//        Recommender recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, similarity);

        ItemSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
        ItemBasedRecommender recommender = new GenericItemBasedRecommender(dataModel, similarity);

        LongPrimitiveIterator iterator = dataModel.getUserIDs();
//        LongPrimitiveIterator iterator = dataModel.getItemIDs();
        while (iterator.hasNext()) {
            long uid = iterator.nextLong();
            long t1 = System.currentTimeMillis();
            List<RecommendedItem> list = recommender.recommend(uid, 5);
            System.out.println(uid);
            for (RecommendedItem item : list) {
                System.out.println(item);
            }
            System.out.println(System.currentTimeMillis() - t1 + "---------------------");
        }

//        File file = new File("D:\\Data\\movie.csv");
//        // 将数据加载到内存中，GroupLensDataModel是针对开放电影评论数据的
//        DataModel dataModel = new GroupLensDataModel(file);
//        // 计算相似度，相似度算法有很多种，欧几里得、皮尔逊等等。
//        UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
//        // 计算最近邻域，邻居有两种算法，基于固定数量的邻居和基于相似度的邻居，这里使用基于固定数量的邻居
//        UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(100, similarity, dataModel);
//        // 构建推荐器，协同过滤推荐有两种，分别是基于用户的和基于物品的，这里使用基于用户的协同过滤推荐
//        Recommender recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, similarity);
//        // 给用户ID等于5的用户推荐10部电影
//        List<RecommendedItem> recommenderItemList = recommender.recommend(5, 10);
//        for (RecommendedItem recommendedItem : recommenderItemList) {
//            System.out.println(recommendedItem);
//        }
    }

    @Test
    public void testUserBasedRecommender() throws IOException, TasteException {
        File file = new File("D:\\Data\\movie.csv");
        DataModel dataModel = new FileDataModel(file);
        ItemSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
        Recommender recommender = new GenericItemBasedRecommender(dataModel, similarity);
        LongPrimitiveIterator iterator = dataModel.getUserIDs();
        while (iterator.hasNext()) {
            long uid = iterator.nextLong();
            long t1 = System.currentTimeMillis();
            List<RecommendedItem> list = recommender.recommend(uid, 5);
            System.out.println(uid);
            for (RecommendedItem item : list) {
                System.out.println(item);
            }
            System.out.println(System.currentTimeMillis() - t1 + "---------------------");
        }
    }

    @Test
    public void testCSV() {
        String path = "D:\\Data\\movie.csv";
//        File file = new File(path);
        try {
            CsvWriter csvWriter = new CsvWriter(path, ',', Charset.forName("GBK"));
            String[] headers = {"user_id", "movie_id", "rating", "timestamp"};
            csvWriter.writeRecord(headers);

            QueryWrapper<MovieRating> queryWrapper = new QueryWrapper<>();
            List<MovieRating> movies = movieRatingMapper.selectList(queryWrapper);
            for (MovieRating movie : movies) {
                System.out.println(movie);
                String[] content = {String.valueOf(movie.getUserId()), String.valueOf(movie.getMovieId()), String.valueOf(movie.getRating()), movie.getTimestamp()};
                csvWriter.writeRecord(content);
            }
            csvWriter.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    void condition() {
        String sqlByTimerUp = "select * from movie order by timer";
        String sqlByTimerDrops = "select * from movie order by timer desc";
        String sqlByRateUp = "select result2.* from (select a.movie_id,score/person as score from \n" +
                " (select movie_id,sum(rating) as score from movie_rating GROUP BY movie_id)as a,\n" +
                " (select movie_id,count(*) as person from movie_rating GROUP BY movie_id) as b \n" +
                " where a.movie_id=b.movie_id )as result1,movie as result2 where result1.movie_id=result2.id order by result1.score";
        String sqlByRateDrops = "select result2.* from (select a.movie_id,score/person as score from \n" +
                " (select movie_id,sum(rating) as score from movie_rating GROUP BY movie_id)as a,\n" +
                " (select movie_id,count(*) as person from movie_rating GROUP BY movie_id) as b \n" +
                " where a.movie_id=b.movie_id )as result1,movie as result2 where result1.movie_id=result2.id order by result1.score desc";
        String sqlByNameUp = "";
        String sqlByNameDrops = "";
    }

    @Test
    public void testSelectStatus() {
        IPage<Movie> page = new Page<>(1, 2);
        String sqlGenreStatus = "";
        int[] sqlTimerStatus = new int[]{90, 120};
        SelectStatusMessage message = new SelectStatusMessage();
        message.setResultSort("hotDrops");
        message.setRating(new int[]{1, 5});
        message.setVote(0);
        message.setTime(sqlTimerStatus);
        message.setCurrentPage(1);
        message.setSize(30);
        sqlGenreStatus = "and m.genre like '%Comedy%Romance'";
        String sqlRating = " ,(select a.movie_id,score/person as score from (select movie_id,sum(rating) as score from movie_rating GROUP BY movie_id)as a," +
                "(select movie_id,count(*) as person from movie_rating GROUP BY movie_id) as b " +
                "where a.movie_id=b.movie_id)as c ";
        String sqlRatingWhereStatus = " and m.id=c.movie_id ";
        String sqlRatingMaxStatus = "and 1<=c.score and c.score<=3 ";

        String sqlTimeStatus = " and " + message.getTime()[0] + "<=m.timer and m.timer<=" + message.getTime()[1] + " ";

        String sqlByHotUp = "select m.* from movie as m,(select movie_id,count(*) as a from movie_rating group by movie_id ) as b" + sqlRating + " where m.id=b.movie_id "
                + sqlRatingWhereStatus + sqlRatingMaxStatus + sqlGenreStatus + sqlTimeStatus + " order by a,m.id";
        String sqlByHotDrops = "select m.* from movie as m,(select movie_id,count(*) as a from movie_rating group by movie_id ) as b where m.id=b.movie_id " + sqlGenreStatus + " order by a desc,m.id";
        System.out.println("-------------------");
        System.out.println(sqlByHotUp);
        System.out.println("-------------------");
        System.out.println(sqlByHotDrops);
        page = movieMapper.getMovieBySelectStatus(page, sqlByHotUp);
        System.out.println(page.getRecords());
        System.out.println("------------------------------");
        page = movieMapper.getMovieBySelectStatus(page, sqlByHotDrops);
        System.out.println(page.getRecords());
    }


    @Test
    public void test() {
        int i = 0;
        i = (i++) + (i++);
        System.out.println(i);
    }
}
