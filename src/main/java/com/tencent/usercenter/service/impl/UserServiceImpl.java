package com.tencent.usercenter.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tencent.usercenter.common.ErrorCode;
import com.tencent.usercenter.exception.BussinessException;
import com.tencent.usercenter.model.User;
import com.tencent.usercenter.service.UserService;
import com.tencent.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author ling
* @description 针对表【user】的数据库操作Service实现
* @createDate 2022-09-05 21:37:27
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;
    private static final String SALT="yupi";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //判断非空
         if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
             throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userAccount.length()<4){
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userPassword.length()<8||checkPassword.length()<8){
            throw new BussinessException(ErrorCode.PARAMS_ERROR,"密码长度小于8");
        }
        //账户不能包含特殊字符
        String regEx = "[ _`~!@#$%^&*()-+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern validPattern = Pattern.compile(regEx);
        Matcher matcher = validPattern.matcher(userAccount);
        if (matcher.find()){
            throw new BussinessException(ErrorCode.PARAMS_ERROR,"含有特殊字符");
        }
        //密码和校验密码相同
        if (!userPassword.equals(checkPassword)){
            throw new BussinessException(ErrorCode.PARAMS_ERROR,"密码和校验密码不相同");
        }

        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount",userAccount);
        long count = this.count(queryWrapper);
        if (count>0){
            return -1;
        }
//        2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean result = this.save(user);
        if (!result){
            return -1;
        }
        return user.getId();
    }

    @Override
    public User doLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //判断非空
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        if (userAccount.length()<4){
            return null;
        }
        if (userPassword.length()<8){
            return null;
        }
        //账户不能包含特殊字符
        String regEx = "[ _`~!@#$%^&*()-+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern validPattern = Pattern.compile(regEx);
        Matcher matcher = validPattern.matcher(userAccount);
        if (matcher.find()){
            return null;
        }

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user==null){
            log.info("user login failed, userAccount cannot match userPassword");
            return null;
        }
        //用户脱敏
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setUserRole(user.getUserRole());
        //记录用户登录状态
        request.getSession().setAttribute(USER_LOGIN_STATUS,user);
//        return user;
        return safetyUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATUS);
        return 1;
    }
}




