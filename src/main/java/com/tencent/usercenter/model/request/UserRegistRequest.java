package com.tencent.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegistRequest implements Serializable {

    private static final long serialVersionUID = -8379292054622850026L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;


}
