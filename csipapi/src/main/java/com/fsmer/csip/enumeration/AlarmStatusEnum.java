package com.fsmer.csip.enumeration;

import lombok.Getter;

@Getter
public enum AlarmStatusEnum {
   START(1,"开始告警"),
    ING(2,"告警中"),
    END(3,"结束告警")
    ;

    private Integer id;
    private String name;
    AlarmStatusEnum(Integer id,String name) {
        this.id=id;
        this.name = name;
    }

}
