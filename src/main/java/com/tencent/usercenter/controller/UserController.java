package com.tencent.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tencent.usercenter.common.BaseResponse;
import com.tencent.usercenter.common.ErrorCode;
import com.tencent.usercenter.common.ResultUtils;
import com.tencent.usercenter.exception.BussinessException;
import com.tencent.usercenter.model.User;
import com.tencent.usercenter.model.request.UserLoginRequest;
import com.tencent.usercenter.model.request.UserRegistRequest;
import com.tencent.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.tencent.usercenter.constant.UserConstant.ADMIN_ROLE;

/**
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegistRequest userRegistRequest){
        if (userRegistRequest==null){
            throw new BussinessException(ErrorCode.PARAMS_NULL);
        }
        String userAccount = userRegistRequest.getUserAccount();
        String userPassword = userRegistRequest.getUserPassword();
        String checkPassword = userRegistRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(id);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest,HttpServletRequest request){
        if (userLoginRequest==null){
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        User user = userService.doLogin(userAccount, userPassword,request);
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if (request==null){
            return null;
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping
    public BaseResponse<List<User>> searchUser(String username,HttpServletRequest request){
        //鉴定权限
        if (!isAdmin(request)){
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }
        List list = userService.list(queryWrapper);
        return ResultUtils.success(list);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody Long id,HttpServletRequest request){
        //鉴定权限
       if (!isAdmin(request)){
           return null;
       }
        if (id<=0){
            return null;
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(UserService.USER_LOGIN_STATUS);
        User currentUser = (User) userObj;
        if (currentUser==null){
            return null;
        }
        User user = userService.getById(currentUser.getId());
        return ResultUtils.success(user);
    }

    public boolean isAdmin(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(UserService.USER_LOGIN_STATUS);
        User user = (User) userObj;
        if (user==null||user.getUserRole() != ADMIN_ROLE){
            return false;
        }
        return true;

    }
}
