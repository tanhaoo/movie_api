package com.th.mapper;

import com.th.bean.Movie;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author TanHaooo
 * @since 2021-03-18
 */
@Component
public interface MovieMapper extends BaseMapper<Movie> {
    Integer getRatePerson();
}
