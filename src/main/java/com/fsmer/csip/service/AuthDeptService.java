package com.fsmer.csip.service;

import com.fsmer.csip.entity.AuthDept;
import com.fsmer.csip.entity.AuthUser;
import com.fsmer.csip.entity.TreeData;
import com.fsmer.csip.entity.response.AuthDeptTree;
import com.fsmer.csip.entity.response.ResponseWrapper;

import java.util.List;

/**
 * 部门服务管理器类
 */
public interface AuthDeptService {

    List<AuthDeptTree> getDeptTreeList(String deptName,String parentDeptId);

    List<AuthDeptTree> getDeptTreeByDeptName(String deptName);

    AuthDept insertAuthDept(AuthDept dept, AuthUser authUser);

    AuthDept updateAuthDept(AuthDept dept, AuthUser authUser);

    ResponseWrapper<List<AuthDept>> deleteAuthDept(String deptIds);

    List<TreeData> getDeptTree(String parentDeptId);
}
