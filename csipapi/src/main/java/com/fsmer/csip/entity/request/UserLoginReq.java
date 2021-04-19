package com.fsmer.csip.entity.request;

import lombok.Data;

@Data
public class UserLoginReq {
    private String username;
    /** 密码 */
    private String password;
}
