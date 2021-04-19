package com.fsmer.csip.entity.response;

import lombok.Data;

import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.List;

@Data
public class AuthMenuTree {
    @Id
    private String menuId;
    /** 权限名称 */
    private String menuName;
    /** 图标 */
    private String menuIcon;
    /** 权限父级 */
    private String parentMenuId;
    /** 访问地址 */
    private String menuUrl;
    /** 是否受保护 */
    private String isProtect;
    /** 是否有效 */
    private String isValid;
    /** 排序号 */
    private Integer sortNum;
    /** 乐观锁 */
    private Integer revision;
    /** 创建人 */
    private String createdBy;
    /** 创建人名称 */
    private String createdByName;
    /** 创建时间 */
    private Timestamp createdTime;
    /** 创建人部门 */
    private String createdDeptId;
    /** 更新人 */
    private String updatedBy;
    /** 更新人名称 */
    private String updatedByName;
    /** 更新时间 */
    private Timestamp updatedTime;
    /** 备注 */
    private String memo;
    private List<AuthMenuTree> children;
    private String id;
    private String title;
}
