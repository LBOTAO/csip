package com.fsmer.csip.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    SUCCESS(0, "操作成功"),
    ERROR(9999, "操作失败"),
    LOGIN_FAILED(9001, "账号或密码不正确"),
    TOKEN_NOT_VALID(9002, "身份鉴权失败"),
    OBJ_ON_EXISTENT(9003, "对象不存在"),
    ROLE_EMPTY(9004, "当前用户没有分配角色"),
    LIST_EMPTY(9005, "集合为空"),
    OBJ_NOT_EQUAL(9006, "对象不一致"),
    PERMISSION_DENIED(9998, "无权限")
    ;

    private final int code;
    private final String msg;
}
