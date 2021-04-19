package com.fsmer.csip.enumeration;

import lombok.Getter;

import java.util.Objects;

/**
 * @program: csipapi
 * @description:
 * @author: Tracy
 * @create: 2021-03-15 11:02
 */
@Getter
public enum AICheckStatusEnum {
    start(1,"start","开启"),
    output(2,"output","开启"),
    unactive(3,"unactive","异常");
    private Integer id;
    private String code;
    private String name;
    AICheckStatusEnum(Integer id,String code,String name) {
        this.id=id;
        this.code = code;
        this.name=name;
    }

    public static  String   getName(String code) {
        for (AICheckStatusEnum messageFlag : AICheckStatusEnum.values()) {
            if (code.equals(messageFlag.code)) {
                return messageFlag.name;
            }
        }
        return "关闭";
    }
}
