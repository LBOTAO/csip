package com.fsmer.csip.entity.response.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fsmer.csip.entity.AuthRole;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
public class UserInfoPage {
    private String userId;
    /** 真实姓名 */
    private String userName;
    /** 登录名 */
    private String loginName;
    /** 登录口令 */
    private String loginPassword;
    /** 是否有效 */
    private String isValid;
    /** 性别 */
    private String genderId;
    /** 生日 */
    private String birthDate;
    /** 公司 */
    private String orgId;
    /** 部门 */
    private String deptId;
    /** 班组 */
    private String workgroupId;
    /** 专业 */
    private String subjectId;
    /** 职务 */
    private String dutyId;
    /** 工号 */
    private String workId;
    /** 卡号 */
    private String cardId;
    /** 住址 */
    private String address;
    /** 邮箱 */
    private String email;
    /** 手机 */
    private String mobilePhone;
    /** 固话 */
    private String fixPhone;
    /** 信息通知方式 */
    private String notifyMode;
    /** 职称 */
    private String titleId;
    /** 文化程度 */
    private String eduLevelId;
    /** 技能等级 */
    private String skillLevelId;
    /** 上级领导 */
    private String managerId;
    /** 排序号 */
    private String sortNum;
    /** 乐观锁 */
    private Integer revision;
    /** 创建人 */
    private String createdBy;
    /** 创建人名称 */
    private String createdByName;
    /** 创建时间 */
    @JsonFormat(timezone = "GMT+8")
    private Timestamp createdTime;
    /** 创建人所在部门 */
    private String createdDeptId;
    /** 更新人 */
    private String updatedBy;
    /** 更新人名称 */
    private String updatedByName;
    /** 更新时间 */
    @JsonFormat(timezone = "GMT+8")
    private Timestamp updatedTime;
    /** 备注 */
    private String memo;
    private List<AuthRole> authRoleList;
    private String deptName;
    private String orgName;
}
