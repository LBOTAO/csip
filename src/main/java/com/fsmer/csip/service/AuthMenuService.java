package com.fsmer.csip.service;

import com.fsmer.csip.entity.AuthMenu;
import com.fsmer.csip.entity.AuthUser;
import com.fsmer.csip.entity.TreeData;
import com.fsmer.csip.entity.response.AuthMenuTree;
import com.fsmer.csip.entity.response.ResponseWrapper;

import java.util.List;

public interface AuthMenuService {
    
    List<AuthMenuTree> getMenuTreeList(String menuName, String parentMenuId);

    List<AuthMenuTree> getMenuTreeByMenuName(String menuName);

    AuthMenu insertAuthMenu(AuthMenu menu, AuthUser authUser);

    AuthMenu updateAuthMenu(AuthMenu menu, AuthUser authUser);

    ResponseWrapper<List<AuthMenu>> deleteAuthMenu(String menuIds);

    List<TreeData> getMenuTree(String parentMenuId);
}
