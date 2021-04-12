package com.th;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.th.bean.Movie;
import com.th.bean.vo.SelectStatusMessage;
import com.th.mapper.MovieMapper;
import com.th.utils.MathUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author TanHaooo
 * @date 2021/4/1 23:14
 */
@SpringBootTest
public class SqlTest {
    @Autowired
    private MovieMapper movieMapper;

    @Test
    public void updateTimer() {
        QueryWrapper wrapper = new QueryWrapper();
        List<Movie> list = movieMapper.selectList(wrapper);
        list.forEach(e -> {
            int timer = 0;
            if (e.getTimer() != null)
                if (e.getTimer().contains(" "))
                    timer = timeVarcharToInt(e.getTimer(), " ");
                else if (e.getTimer().contains("h"))
                    timer = timeVarcharToInt(e.getTimer(), "h");
                else if (e.getTimer().contains("min"))
                    timer = timeVarcharToInt(e.getTimer(), "min");
            System.out.println(timer + "|" + e.getTimer());
            e.setTimer(String.valueOf(timer));
            movieMapper.updateById(e);
            System.out.println("--------------------------");
        });
    }

    public int timeVarcharToInt(String time, String split) {
        String hour, min;
        switch (split) {
            case " ":
                hour = time.split(split)[0];
                min = time.split(split)[1];
                return Integer.parseInt(hour.substring(0, hour.length() - 1)) * 60 +
                        Integer.parseInt(min.substring(0, min.length() - 3));
            case "h":
                hour = time.split(split)[0];
                return Integer.parseInt(hour) * 60;
            case "min":
                min = time.split(split)[0];
                return Integer.parseInt(min);
        }
        return 0;
    }

    @Test
    public void testSqlGenreString(){
        String sqlGenreStatus = "and m.genre like '%";
        //Comedy%Romance'
        SelectStatusMessage message=new SelectStatusMessage();
        message.setType(new String[]{"Musical","Romance"});
        for (String s : message.getType()) {
            sqlGenreStatus += (s + "%");
        }
        sqlGenreStatus += "'";
        System.out.println(sqlGenreStatus);
    }

    @Test
    public void test(){
        System.err.println(12 - 11.9 == 0.1);
    }

    public void charTO(String i){
        System.out.println(i);
    }
}
