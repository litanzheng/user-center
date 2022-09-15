package com.tencent.usercenter.service;

import com.tencent.usercenter.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author ling
* @description 针对表【user】的数据库操作Service
* @createDate 2022-09-05 21:37:27
*/
public interface UserService extends IService<User> {

    public static final String USER_LOGIN_STATUS="userLoginStatus";

    /**
     * 用户注册
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return              新用户id
     */
    long userRegister(String userAccount,String userPassword,String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return User
     */
    User doLogin(String userAccount, String userPassword, HttpServletRequest request);


    /**
     * 用户注销
     * @param request
     */
    int userLogout(HttpServletRequest request);
}
