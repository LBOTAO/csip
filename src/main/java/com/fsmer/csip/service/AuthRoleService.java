package com.fsmer.csip.service;

import com.fsmer.csip.entity.AuthRole;
import com.fsmer.csip.entity.AuthUser;
import com.fsmer.csip.entity.TreeData;
import com.fsmer.csip.entity.response.AuthRoleTree;
import com.fsmer.csip.entity.response.ResponseWrapper;

import java.util.List;

public interface AuthRoleService {
    boolean hasOneOfRoles(AuthUser authUser, String... roleName);
    List<AuthRole> findByUserId(String userId);
    
    List<AuthRoleTree> getRoleTreeList(String roleName, String parentRoleId);

    List<AuthRoleTree> getRoleTreeByRoleName(String roleName);

    AuthRole insertAuthRole(AuthRole role, AuthUser authUser);

    AuthRole updateAuthRole(AuthRole role, AuthUser authUser);

    ResponseWrapper<List<AuthRole>> deleteAuthRole(String roleIds);

    List<TreeData> getRoleTree(String parentRoleId);

    List<TreeData> getDefaultRoleTree(String parentRoleId);

    List<AuthRole> getDefaultRole();
}
