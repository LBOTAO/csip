package com.fsmer.csip.service.impl;

import com.fsmer.csip.entity.AuthRoleMenu;
import com.fsmer.csip.entity.AuthUser;
import com.fsmer.csip.entity.TreeData;
import com.fsmer.csip.entity.response.AuthMenuTree;
import com.fsmer.csip.enumeration.ResponseCode;
import com.fsmer.csip.repository.AuthRoleMenuRepository;
import com.fsmer.csip.service.AuthMenuService;
import com.fsmer.csip.service.AuthRoleMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthRoleMenuServiceImpl implements AuthRoleMenuService {
    @Autowired
    private AuthRoleMenuRepository authRoleMenuRepository;
    @Autowired
    private AuthMenuService authMenuService;

    /**
     * 新增
     * @param map 对象
     * @param authUser 当前用户
     * @return
     */
    @Override
    public List<AuthRoleMenu> updateRoleMenu(Map<String,Object> map, AuthUser authUser){
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        String userId = authUser.getUserId();
        String roleId = map.get("roleId").toString();
        deleteRoleMenu(roleId);
        List<String> menuIdList = (ArrayList<String>)map.get("menuIds");
        if(CollectionUtils.isEmpty(menuIdList)){
            return null;
        }
        List<AuthRoleMenu> roleMenuList = new ArrayList<>();
        for(String menuId : menuIdList){
            AuthRoleMenu roleMenu = new AuthRoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenu.setRevision(0);
            roleMenu.setCreatedBy(userId);
            roleMenu.setCreatedTime(currentTime);
            roleMenuList.add(roleMenu);
        }
        List<AuthRoleMenu> result = authRoleMenuRepository.saveAll(roleMenuList);
        return result;
    }

    /**
     * 删除
     * @param roleId 角色id
     * @return
     */
    @Override
    public String deleteRoleMenu(String roleId){
        if(StringUtils.isEmpty(roleId)){
            return ResponseCode.OBJ_ON_EXISTENT.getMsg();
        }
        try{
            List<AuthRoleMenu> roleMenuList = authRoleMenuRepository.findByRoleId(roleId);
            if(CollectionUtils.isEmpty(roleMenuList)){
                return ResponseCode.OBJ_ON_EXISTENT.getMsg();
            }
            authRoleMenuRepository.deleteAll(roleMenuList);
        }catch(Exception e){
            return ResponseCode.ERROR.getMsg();
        }
        return ResponseCode.SUCCESS.getMsg();
    }

    /**
     * 根据角色获取角色菜单列表
     * @param roleId 角色id
     * @return
     */
    public List<AuthRoleMenu> getRoleMenuList(String roleId){
        List<AuthRoleMenu> roleMenuList = authRoleMenuRepository.findByRoleId(roleId);
        return roleMenuList;
    }

    /**
     * 根据角色获取角色菜单列表
     * @param roleId 角色id
     * @return
     */
    @Override
    public List<TreeData> getRoleMenuTree(String roleId){
        List<AuthRoleMenu> roleMenuList = authRoleMenuRepository.findByRoleId(roleId);
        String menuIds = roleMenuList.stream().map(AuthRoleMenu::getMenuId).collect(Collectors.joining());
        List<TreeData> menuTreeList = authMenuService.getMenuTree("root");
        if(StringUtils.isEmpty(menuIds)){
            return menuTreeList;
        }
        menuTreeList.forEach(item -> {
            String menuId = item.getValue();
            if(menuIds.contains(menuId)){
                item.setChecked(true);
                item.setSelected(true);
            }
            List<TreeData> children = item.getChildren();
            children.forEach(child -> {
                String id = child.getValue();
                if (menuIds.contains(id)) {
                    child.setChecked(true);
                    child.setSelected(true);
                }
            });
        });
        return menuTreeList;
    }

}
