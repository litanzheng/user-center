package com.tencent.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {


    private static final long serialVersionUID = 7842314983061739133L;
    private String userAccount;
    private String userPassword;


}
