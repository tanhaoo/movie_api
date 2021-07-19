package com.th.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.th.bean.User;
import com.th.mapper.UserMapper;
import com.th.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.th.utils.IPAddressUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author TanHaooo
 * @since 2021-03-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Value("${upload.path}")
    private String uploadPath;

    public User login(String username, String password) {
        return userMapper.login(username, password);
    }

    @Override
    public String upload(MultipartFile file, String userName) {
        String originalFilename = file.getOriginalFilename();
        // 校验文件的类型
        String contentType = file.getContentType();
        try {
            // 校验文件的内容
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null) {
                System.err.println("文件内容不合法：" + originalFilename);
                return null;
            }
            // 保存到服务器
            file.transferTo(new File(uploadPath + originalFilename));
            // 生成url地址，返回
            //更新User表img
            User user = new User();
            user.setUserName(userName);
            user.setImg("/img/" + originalFilename);
            userMapper.update(user, new QueryWrapper<User>().eq("user_name", user.getUserName()));
            return IPAddressUtil.getServeURL() + "/img/" + originalFilename;
        } catch (IOException e) {
            System.err.println("服务器内部错误：" + originalFilename);
            e.printStackTrace();
        }
        return null;
    }

}
