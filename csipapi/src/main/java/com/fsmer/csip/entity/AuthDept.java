package com.fsmer.csip.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Data
public class AuthDept {
    @Id
    private String deptId;
    /** 部门名称 */
    private String deptName;
    /** 部门简称 */
    private String deptNameShort;
    /** 所在公司 */
    private String orgId;
    /** 所在公司名称 */
    private String orgName;
    /** 上级编号 */
    private String parentDeptId;
    /** 部门类型 */
    private String deptTypeId;
    /** 部门类型名称 */
    private String deptTypeName;
    /** 部门编码 */
    private String deptCode;
    /** 子节点数 */
    private Integer childNodeNum;
    /** 是否有效 */
    private String isValid;
    /** 排序号 */
    private Integer sortNum;
    /** 乐观锁 */
    private Integer revision;
    /** 创建人 */
    private String createdBy;
    /** 创建时间 */
    private Timestamp createdTime;
    /** 更新人 */
    private String updatedBy;
    /** 更新时间 */
    private Timestamp updatedTime;
    /** 备注 */
    private String memo;


}
