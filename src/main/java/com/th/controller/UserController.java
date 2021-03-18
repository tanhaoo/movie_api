package com.th.controller;


import com.alibaba.fastjson.JSONObject;
import com.th.bean.User;
import com.th.bean.vo.Permission;
import com.th.bean.vo.Permissions;
import com.th.bean.vo.UserInfo;
import com.th.service.UserService;
import com.th.utils.retunJson.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        user.setToken(user.getUserName());
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
        UserInfo userInfo = new UserInfo(user.getUserName(), permissions);
        return JSONObject.toJSONString(new ReturnObject(userInfo));
    }

    @RequestMapping("/logout")
    public void logOut(HttpSession session) {
        session.invalidate();
    }

}

