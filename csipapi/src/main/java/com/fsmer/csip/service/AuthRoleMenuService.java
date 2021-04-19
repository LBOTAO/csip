package com.fsmer.csip.service;

import com.fsmer.csip.entity.AuthRoleMenu;
import com.fsmer.csip.entity.AuthUser;
import com.fsmer.csip.entity.TreeData;
import com.fsmer.csip.entity.response.AuthMenuTree;

import java.util.List;
import java.util.Map;

public interface AuthRoleMenuService {

    List<AuthRoleMenu> updateRoleMenu(Map<String,Object> map, AuthUser authUser);

    String deleteRoleMenu(String roleIds);

    List<AuthRoleMenu> getRoleMenuList(String roleId);

    List<TreeData> getRoleMenuTree(String roleId);
}
