package com.fsmer.csip.service.impl;

import com.fsmer.csip.entity.AuthRole;
import com.fsmer.csip.entity.AuthUser;
import com.fsmer.csip.entity.TreeData;
import com.fsmer.csip.entity.response.AuthRoleTree;
import com.fsmer.csip.entity.response.ResponseWrapper;
import com.fsmer.csip.enumeration.ResponseCode;
import com.fsmer.csip.repository.AuthRoleRepository;
import com.fsmer.csip.service.AuthRoleService;
import com.fsmer.csip.service.AuthUserRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class AuthRoleServiceImpl implements AuthRoleService {

    @Autowired
    private AuthRoleRepository authRoleRepository;
    @Autowired
    private AuthUserRoleService authUserRoleService;

    @Override
    public boolean hasOneOfRoles(AuthUser authUser, String... roleNames) {
        if (roleNames.length == 0) return false;

        List<String> roleNameList = Arrays.asList(roleNames);
        List<AuthRole> authRoleList = findByUserId(authUser.getUserId());
        boolean hasRoleFlag = false;
        for (AuthRole authRole : authRoleList) {
            if (roleNameList.get(0).contains(authRole.getRoleName())){
                hasRoleFlag = true;
                break;
            }
        }
        return hasRoleFlag;
    }

    @Override
    public List<AuthRole> findByUserId(String userId) {
        List<String> roleIdList = authUserRoleService.findUserRolesByUserId(userId);
        if(CollectionUtils.isEmpty(roleIdList)){
            return new ArrayList<>();
        }
        return authRoleRepository.findAllById(roleIdList);
    }

    /**
     * 获取部门下拉树形结构
     * @return
     */
    @Override
    public List<AuthRoleTree> getRoleTreeList(String roleName, String parentRoleId){
        List<AuthRoleTree> roleTreeList;
        if(StringUtils.isNotEmpty(roleName)){
            roleTreeList = getRoleTreeByRoleName(roleName);
            return roleTreeList;
        }
        parentRoleId = parentRoleId == null ? "root" : parentRoleId;
        roleTreeList = getRoleByParentId(parentRoleId);
        if(!CollectionUtils.isEmpty(roleTreeList)){
            for (int i = 0; i <roleTreeList.size() ; i++) {
                List<AuthRoleTree> roleChildrenList = getRoleTreeList("",roleTreeList.get(i).getRoleId());
                if(!CollectionUtils.isEmpty(roleChildrenList)){
                    roleTreeList.get(i).setChildren(roleChildrenList);
                }
            }
        }
        return roleTreeList;
    }

    /**
     * 新增
     * @param role 对象
     * @param authUser 当前用户
     * @return
     */
    @Override
    public AuthRole insertAuthRole(AuthRole role, AuthUser authUser){
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        String userId = authUser.getUserId();
        role.setRoleId(UUID.randomUUID().toString());
        role.setCreatedTime(currentTime);
        role.setCreatedBy(userId);
        role.setUpdatedBy(userId);
        role.setUpdatedTime(currentTime);
        role.setRevision(0);
        authRoleRepository.save(role);
        return role;
    }

    /**
     * 更新
     * @param role 对象
     * @param authUser 当前用户
     * @return
     */
    @Override
    public AuthRole updateAuthRole(AuthRole role, AuthUser authUser){
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        String userId = authUser.getUserId();
        role.setUpdatedBy(userId);
        role.setUpdatedTime(currentTime);
        authRoleRepository.save(role);
        return role;
    }

    /**
     * 删除
     * @param roleIds ids
     * @return
     */
    @Override
    public ResponseWrapper<List<AuthRole>> deleteAuthRole(String roleIds){
        if(StringUtils.isEmpty(roleIds)){
            return ResponseWrapper.createByErrorCodeMessage(ResponseCode.OBJ_ON_EXISTENT.getCode(),"对象不存在");
        }
        List<AuthRole> roleList;
        try{
            String[] roleIdArr = roleIds.split(",");
            roleList = authRoleRepository.findAllById(Arrays.asList(roleIdArr));
            authRoleRepository.deleteAll(roleList);
        }catch(Exception e){
            return ResponseWrapper.createByErrorCodeMessage(ResponseCode.ERROR.getCode(),"删除失败");
        }
        return ResponseWrapper.createBySuccessCodeMessage("删除成功",roleList);
    }

    @Override
    public List<AuthRoleTree> getRoleTreeByRoleName(String roleName) {
        List<AuthRole> roleList = authRoleRepository.findByRoleNameLike("%" + roleName + "%");
        List<AuthRoleTree> roleTreeList = copyRoleToRoleTree(roleList);
        return roleTreeList;
    }

    /**
     * 获取下拉树形结构
     * @return
     */
    @Override
    public List<TreeData> getDefaultRoleTree(String parentRoleId){
        List<TreeData> treeDataListNew = new ArrayList<>();
        List<TreeData> treeDataList = getRoleTreeChildren(parentRoleId,true);
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
     * 获取下拉树形结构
     * @return
     */
    @Override
    public List<TreeData> getRoleTree(String parentRoleId){
        List<TreeData> treeDataListNew = new ArrayList<>();
        List<TreeData> treeDataList = getRoleTreeChildren(parentRoleId,false);
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
     * 获取默认角色列表
     * @return
     */
    @Override
    public List<AuthRole> getDefaultRole(){
        List<AuthRole> roleList = authRoleRepository.findByIsDefault("1");
        return roleList;
    }


    /**
     * 获取下拉树形结构，
     * @return
     */
    public List<TreeData> getRoleTreeChildren(String parentRoleId,boolean isHaveDefault){
        parentRoleId = parentRoleId == null ? "root" : parentRoleId;
        List<TreeData> treeDataList = getTreeDataByParentId(parentRoleId,isHaveDefault);
        if(!CollectionUtils.isEmpty(treeDataList)){
            for (int i = 0; i <treeDataList.size() ; i++) {
                List<TreeData> childrenList = getRoleTreeChildren(treeDataList.get(i).getValue(),isHaveDefault);
                if(!CollectionUtils.isEmpty(childrenList)){
                    treeDataList.get(i).setChildren(childrenList);
                }
            }
        }
        return treeDataList;
    }
    /**
     * 根据父级获取下级子节点
     * @param parentRoleId 父级
     * @return
     */
    public List<AuthRoleTree> getRoleByParentId(String parentRoleId){
        List<AuthRole> roleList = authRoleRepository.findByParentRoleId(parentRoleId);
        List<AuthRoleTree> roleTreeList = copyRoleToRoleTree(roleList);
        return roleTreeList;
    }

    /**
     * 将部门对象转换成树形对象
     * @param parentRoleId 父级
     * @return
     */
    public List<TreeData> getTreeDataByParentId(String parentRoleId,boolean isHaveDefault){
        List<AuthRole> roleList = authRoleRepository.findByParentRoleId(parentRoleId);
        List<TreeData> treeDataList = copyRoleToTreeData(roleList,isHaveDefault);
        return treeDataList;
    }

    public List<AuthRoleTree> copyRoleToRoleTree(List<AuthRole> roleList){
        if(CollectionUtils.isEmpty(roleList)){
            return new ArrayList<>();
        }
        List<AuthRoleTree> roleTreeList = new ArrayList<>();
        roleList.forEach(item -> {
            AuthRoleTree roleTree = new AuthRoleTree();
            BeanUtils.copyProperties(item,roleTree);
            roleTreeList.add(roleTree);
        });
        return roleTreeList;
    }

    public List<TreeData> copyRoleToTreeData(List<AuthRole> roleList,boolean isHaveDefault){
        if(CollectionUtils.isEmpty(roleList)){
            return new ArrayList<>();
        }
        List<TreeData> treeDataList = new ArrayList<>();
        roleList.forEach(item -> {
            boolean checked = isHaveDefault && "1".equals(item.getIsDefault());
            boolean selected = isHaveDefault && "1".equals(item.getIsDefault());
//            boolean disabled = isHaveDefault && "1".equals(item.getIsDefault());
            TreeData treeData = new TreeData();
            treeData.setChecked(checked);
            treeData.setExpand(false);
            treeData.setSelected(selected);
            treeData.setTitle(item.getRoleName());
            treeData.setValue(item.getRoleId());
//            treeData.setDisabled(disabled);
            treeDataList.add(treeData);
        });
        return treeDataList;
    }
}
