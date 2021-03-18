package com.th.mapper;

import com.th.bean.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
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
public interface UserMapper extends BaseMapper<User> {
    User login(@Param("username") String username, @Param("password") String password);
}
