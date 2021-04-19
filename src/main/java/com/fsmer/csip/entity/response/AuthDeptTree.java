package com.fsmer.csip.entity.response;

import com.fsmer.csip.entity.AuthDept;
import lombok.Data;

import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.List;

@Data
public class AuthDeptTree {

    @Id
    private String deptId;
    private String deptName;
    private String deptNameShort;
    private String orgId;
    private String orgName;
    private String parentDeptId;
    private String deptTypeId;
    private String deptTypeName;
    private String deptCode;
    private Integer childNodeNum;
    private String isValid;
    private Integer sortNum;
    private Integer revision;
    private String createdBy;
    private Timestamp createdTime;
    private String updatedBy;
    private Timestamp updatedTime;
    private String memo;
    private List<AuthDeptTree> children;
}
