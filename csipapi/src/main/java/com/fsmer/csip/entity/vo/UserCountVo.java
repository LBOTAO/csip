package com.fsmer.csip.entity.vo;

import lombok.Data;

/**
 * @program: rules
 * @description:
 * @author: Tracy
 * @create: 2020-08-12 14:24
 */

@Data
public class UserCountVo {
    private Integer deptId;
    private Long count;
    private Integer userId;
    public UserCountVo(){}
    public  UserCountVo(Integer deptId,Long count,Integer userId){
        this.count=count;
        this.deptId=deptId;
        this.userId=userId;
    }
}
