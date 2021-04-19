package com.fsmer.csip.entity.vo;

import lombok.Data;

@Data
public class RoleVO {
    private String roleId;
    /** 角色名称 */
    private String roleName;
    /** 父级 */
    private String parentId;
    public RoleVO(){}
    public RoleVO(String roleId, String roleName, String parentId){
        this.parentId=parentId;
        this.roleName=roleName;
        this.roleId=roleId;
    }
}
