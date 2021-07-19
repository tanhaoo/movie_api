package com.th.service;

import com.th.bean.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author TanHaooo
 * @since 2021-03-18
 */
public interface UserService extends IService<User> {

    User login(String username, String password);

    String upload(MultipartFile file,String userName);
}
