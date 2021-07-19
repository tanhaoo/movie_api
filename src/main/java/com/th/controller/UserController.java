package com.th.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.th.bean.User;
import com.th.bean.vo.Permission;
import com.th.bean.vo.Permissions;
import com.th.bean.vo.UserInfo;
import com.th.service.UserService;
import com.th.utils.IPAddressUtil;
import com.th.utils.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author TanHaooo
 * @since 2021-03-18
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/2step-code")
    public Boolean test() {
        System.out.println("前端框架自带的一个验证规则，写不写无所谓");
        return true;
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) {
        System.out.println("login");
        User user = userService.login(username, password);
        user.setToken(String.valueOf(user.getId()));
        session.setAttribute("userRecord", user);
        return JSONObject.toJSONString(new ReturnObject(user));
    }

    @RequestMapping("/info")
    public String getInfo(HttpSession session) {
        User user = (User) session.getAttribute("userRecord");
        //获取模块信息
        String[] split = user.getRolePrivileges().split("-");
        //创建权限集合对象
        Permissions permissions = new Permissions();
        List<Permission> permissionList = new ArrayList<>();
        for (String s : split) {
            permissionList.add(new Permission(s));
        }
        permissions.setPermissions(permissionList);
        UserInfo userInfo = new UserInfo(user.getUserName(), IPAddressUtil.getServeURL() + user.getImg(), permissions);
        return JSONObject.toJSONString(new ReturnObject(userInfo));
    }

    @RequestMapping("/logout")
    public void logOut(HttpSession session) {
        session.invalidate();
    }

    @PostMapping("/uploadImage")
    public String uploadImage(@RequestParam("avatar") MultipartFile file, HttpSession session) {
        User user = (User) session.getAttribute("userRecord");
        return JSONObject.toJSONString(new ReturnObject(userService.upload(file, user.getUserName())));
    }

    @PostMapping("updateUserByName")
    public String updateUserByName(@RequestBody User user) {
        return JSONObject.toJSONString(new ReturnObject(
                userService.update(user, new QueryWrapper<User>()
                        .eq("user_name", user.getUserName())) == true
                        ? "用户信息修改成功"
                        : "用户信息修改失败"
        ));
    }

    @PostMapping("register")
    public String register(@RequestBody User user) {
        user.setImg("/img/desktop-1.jpg");
        user.setRolePrivileges("901-221");
        return JSONObject.toJSONString(new ReturnObject(userService.save(user)));
    }

}

