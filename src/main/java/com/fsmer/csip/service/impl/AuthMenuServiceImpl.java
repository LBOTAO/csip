package com.fsmer.csip.service.impl;

import com.fsmer.csip.entity.AuthMenu;
import com.fsmer.csip.entity.AuthUser;
import com.fsmer.csip.entity.TreeData;
import com.fsmer.csip.entity.response.AuthMenuTree;
import com.fsmer.csip.entity.response.ResponseWrapper;
import com.fsmer.csip.enumeration.ResponseCode;
import com.fsmer.csip.repository.AuthMenuRepository;
import com.fsmer.csip.service.AuthMenuService;
import com.fsmer.csip.util.DateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.*;

@Service
public class AuthMenuServiceImpl implements AuthMenuService {
    @Autowired
    private AuthMenuRepository authMenuRepository;


    /**
     * 获取部门下拉树形结构
     * @return
     */
    @Override
    public List<AuthMenuTree> getMenuTreeList(String menuName, String parentMenuId){
        List<AuthMenuTree> menuTreeList;
        if(StringUtils.isNotEmpty(menuName)){
            menuTreeList = getMenuTreeByMenuName(menuName);
            return menuTreeList;
        }
        parentMenuId = parentMenuId == null ? "root" : parentMenuId;
        menuTreeList = getMenuByParentId(parentMenuId);
        if(!CollectionUtils.isEmpty(menuTreeList)){
            for (int i = 0; i <menuTreeList.size() ; i++) {
                List<AuthMenuTree> menuChildrenList = getMenuTreeList("",menuTreeList.get(i).getMenuId());
                if(!CollectionUtils.isEmpty(menuChildrenList)){
                    menuTreeList.get(i).setChildren(menuChildrenList);
                }
            }
        }
        return menuTreeList;
    }

    /**
     * 新增
     * @param menu 对象
     * @param authUser 当前用户
     * @return
     */
    @Override
    public AuthMenu insertAuthMenu(AuthMenu menu, AuthUser authUser){
        String userId = authUser.getUserId();
        menu.setMenuId(UUID.randomUUID().toString());
        menu.setCreatedTime(DateTimeUtil.toDay());
        menu.setCreatedBy(userId);
        menu.setUpdatedBy(userId);
        menu.setUpdatedTime(DateTimeUtil.toDay());
        menu.setRevision(0);
        authMenuRepository.save(menu);
        return menu;
    }

    /**
     * 更新
     * @param menu 对象
     * @param authUser 当前用户
     * @return
     */
    @Override
    public AuthMenu updateAuthMenu(AuthMenu menu, AuthUser authUser){
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        String userId = authUser.getUserId();
        menu.setUpdatedBy(userId);
        menu.setUpdatedTime(currentTime);
        authMenuRepository.save(menu);
        return menu;
    }

    /**
     * 删除
     * @param menuIds ids
     * @return
     */
    @Override
    public ResponseWrapper<List<AuthMenu>> deleteAuthMenu(String menuIds){
        if(StringUtils.isEmpty(menuIds)){
            return ResponseWrapper.createByErrorCodeMessage(ResponseCode.OBJ_ON_EXISTENT.getCode(),"对象不存在");
        }
        List<AuthMenu> menuList;
        try{
            String[] menuIdArr = menuIds.split(",");
            menuList = authMenuRepository.findAllById(Arrays.asList(menuIdArr));
            authMenuRepository.deleteAll(menuList);
        }catch(Exception e){
            return ResponseWrapper.createByErrorCodeMessage(ResponseCode.ERROR.getCode(),"删除失败");
        }
        return ResponseWrapper.createBySuccessCodeMessage("删除成功",menuList);
    }

    @Override
    public List<AuthMenuTree> getMenuTreeByMenuName(String menuName) {
        List<AuthMenu> menuList = authMenuRepository.findByMenuNameLike("%" + menuName + "%");
        List<AuthMenuTree> menuTreeList = copyMenuToMenuTree(menuList);
        menuTreeList.stream().sorted(Comparator.comparing(AuthMenuTree :: getSortNum));
        return menuTreeList;
    }

    /**
     * 获取部门下拉树形结构
     * @return
     */
    @Override
    public List<TreeData> getMenuTree(String parentMenuId){
        List<TreeData> treeDataListNew = new ArrayList<>();
        List<TreeData> treeDataList = getMenuTreeChildren(parentMenuId);
        TreeData treeRoot = new TreeData();
        treeRoot.setValue("root");
        treeRoot.setTitle("root");
        treeRoot.setSelected(false);
        treeRoot.setExpand(true);
        treeRoot.setChecked(false);
        treeRoot.setChildren(treeDataList);
        treeDataListNew.add(treeRoot);
        return treeDataListNew;
    }

    /**
     * 获取部门下拉树形结构
     * @return
     */
    public List<TreeData> getMenuTreeChildren(String parentMenuId){
        parentMenuId = parentMenuId == null ? "root" : parentMenuId;
        List<TreeData> treeDataList = getTreeDataByParentId(parentMenuId);
        if(!CollectionUtils.isEmpty(treeDataList)){
            for (int i = 0; i <treeDataList.size() ; i++) {
                List<TreeData> childrenList = getMenuTreeChildren(treeDataList.get(i).getValue());
                if(!CollectionUtils.isEmpty(childrenList)){
                    treeDataList.get(i).setChildren(childrenList);
                }
            }
        }
        return treeDataList;
    }
    /**
     * 根据父级获取下级子节点
     * @param parentMenuId 父级
     * @return
     */
    public List<AuthMenuTree> getMenuByParentId(String parentMenuId){
        List<AuthMenu> menuList = authMenuRepository.findByParentMenuId(parentMenuId);
        List<AuthMenuTree> menuTreeList = copyMenuToMenuTree(menuList);
        menuTreeList.stream().sorted(Comparator.comparing(AuthMenuTree :: getSortNum));
        return menuTreeList;
    }

    /**
     * 将部门对象转换成树形对象
     * @param parentMenuId 父级
     * @return
     */
    public List<TreeData> getTreeDataByParentId(String parentMenuId){
        List<AuthMenu> menuList = authMenuRepository.findByParentMenuId(parentMenuId);
        List<TreeData> treeDataList = copyMenuToTreeData(menuList);
        treeDataList.stream().sorted(Comparator.comparing(TreeData :: getSortNum));
        return treeDataList;
    }

    public List<AuthMenuTree> copyMenuToMenuTree(List<AuthMenu> menuList){
        if(CollectionUtils.isEmpty(menuList)){
            return new ArrayList<>();
        }
        List<AuthMenuTree> menuTreeList = new ArrayList<>();
        menuList.forEach(item -> {
            AuthMenuTree menuTree = new AuthMenuTree();
            BeanUtils.copyProperties(item,menuTree);
            menuTree.setId(menuTree.getMenuId());
            menuTree.setTitle(menuTree.getMenuName());
            menuTreeList.add(menuTree);
        });
        return menuTreeList;
    }

    public List<TreeData> copyMenuToTreeData(List<AuthMenu> menuList){
        if(CollectionUtils.isEmpty(menuList)){
            return new ArrayList<>();
        }
        List<TreeData> treeDataList = new ArrayList<>();
        menuList.forEach(item -> {
            TreeData treeData = new TreeData();
            treeData.setChecked(false);
            treeData.setExpand(false);
            treeData.setSelected(false);
            treeData.setTitle(item.getMenuName());
            treeData.setValue(item.getMenuId());
            treeData.setSortNum(item.getSortNum());
            treeDataList.add(treeData);
        });
        return treeDataList;
    }
}
