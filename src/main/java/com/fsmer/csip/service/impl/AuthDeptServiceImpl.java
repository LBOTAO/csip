package com.fsmer.csip.service.impl;

import com.fsmer.csip.entity.AuthDept;
import com.fsmer.csip.entity.AuthUser;
import com.fsmer.csip.entity.TreeData;
import com.fsmer.csip.entity.response.AuthDeptTree;
import com.fsmer.csip.entity.response.ResponseWrapper;
import com.fsmer.csip.enumeration.ResponseCode;
import com.fsmer.csip.repository.AuthDeptRepository;
import com.fsmer.csip.service.AuthDeptService;
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

/**
 * 部门服务管理器实现类
 */
@Service
public class AuthDeptServiceImpl implements AuthDeptService {

    @Autowired
    AuthDeptRepository authDeptRepository;

    /**
     * 获取部门下拉树形结构
     * @return
     */
    @Override
    public List<AuthDeptTree> getDeptTreeList(String deptName,String parentDeptId){
        List<AuthDeptTree> deptTreeList;
        if(StringUtils.isNotEmpty(deptName)){
            deptTreeList = getDeptTreeByDeptName(deptName);
            return deptTreeList;
        }
        parentDeptId = parentDeptId == null ? "root" : parentDeptId;
        deptTreeList = getDeptByParentId(parentDeptId);
        if(!CollectionUtils.isEmpty(deptTreeList)){
            for (int i = 0; i <deptTreeList.size() ; i++) {
                List<AuthDeptTree> deptChildrenList = getDeptTreeList("",deptTreeList.get(i).getDeptId());
                if(!CollectionUtils.isEmpty(deptChildrenList)){
                    deptTreeList.get(i).setChildren(deptChildrenList);
                }
            }
        }
        return deptTreeList;
    }

    /**
     * 新增部门信息
     * @param dept 部门对象
     * @param authUser 当前用户
     * @return
     */
    @Override
    public AuthDept insertAuthDept(AuthDept dept, AuthUser authUser){
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        String userId = authUser.getUserId();
        dept.setDeptId(UUID.randomUUID().toString());
        dept.setCreatedTime(currentTime);
        dept.setCreatedBy(userId);
        dept.setUpdatedBy(userId);
        dept.setUpdatedTime(currentTime);
        dept.setDeptCode(dept.getDeptId());
        dept.setRevision(0);
        authDeptRepository.save(dept);
        return dept;
    }

    /**
     * 新增部门信息
     * @param dept 部门对象
     * @param authUser 当前用户
     * @return
     */
    @Override
    public AuthDept updateAuthDept(AuthDept dept, AuthUser authUser){
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        String userId = authUser.getUserId();
        dept.setUpdatedBy(userId);
        dept.setUpdatedTime(currentTime);
        authDeptRepository.save(dept);
        return dept;
    }

    /**
     * 删除部门信息
     * @param deptIds 部门ids
     * @return
     */
    @Override
    public ResponseWrapper<List<AuthDept>> deleteAuthDept(String deptIds){
        if(StringUtils.isEmpty(deptIds)){
            return ResponseWrapper.createByErrorCodeMessage(ResponseCode.OBJ_ON_EXISTENT.getCode(),"对象不存在");
        }
        List<AuthDept> deptList;
        try{
            String[] deptIdArr = deptIds.split(",");
            deptList = authDeptRepository.findAllById(Arrays.asList(deptIdArr));
            authDeptRepository.deleteAll(deptList);
        }catch(Exception e){
            return ResponseWrapper.createByErrorCodeMessage(ResponseCode.ERROR.getCode(),"删除失败");
        }
        return ResponseWrapper.createBySuccessCodeMessage("删除成功",deptList);
    }

    @Override
    public List<AuthDeptTree> getDeptTreeByDeptName(String deptName) {
        List<AuthDept> deptList = authDeptRepository.findByDeptNameLike("%" + deptName + "%");
        List<AuthDeptTree> deptTreeList = copyDeptToDeptTree(deptList);
        return deptTreeList;
    }

    /**
     * 获取部门下拉树形结构
     * @return
     */
    @Override
    public List<TreeData> getDeptTree(String parentDeptId){
        List<TreeData> treeDataListNew = new ArrayList<>();
        List<TreeData> treeDataList = getDeptTreeChildren(parentDeptId);
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
    public List<TreeData> getDeptTreeChildren(String parentDeptId){
        parentDeptId = parentDeptId == null ? "root" : parentDeptId;
        List<TreeData> treeDataList = getTreeDataByParentId(parentDeptId);
        if(!CollectionUtils.isEmpty(treeDataList)){
            for (int i = 0; i <treeDataList.size() ; i++) {
                List<TreeData> childrenList = getDeptTreeChildren(treeDataList.get(i).getValue());
                if(!CollectionUtils.isEmpty(childrenList)){
                    treeDataList.get(i).setChildren(childrenList);
                }
            }
        }
        return treeDataList;
    }
    /**
     * 根据父级获取下级子节点
     * @param parentDeptId 父级
     * @return
     */
    public List<AuthDeptTree> getDeptByParentId(String parentDeptId){
        List<AuthDept> deptList = authDeptRepository.findByParentDeptId(parentDeptId);
        List<AuthDeptTree> deptTreeList = copyDeptToDeptTree(deptList);
        return deptTreeList;
    }

    /**
     * 将部门对象转换成树形对象
     * @param parentDeptId 父级
     * @return
     */
    public List<TreeData> getTreeDataByParentId(String parentDeptId){
        List<AuthDept> deptList = authDeptRepository.findByParentDeptId(parentDeptId);
        List<TreeData> treeDataList = copyDeptToTreeData(deptList);
        return treeDataList;
    }

    public List<AuthDeptTree> copyDeptToDeptTree(List<AuthDept> deptList){
        if(CollectionUtils.isEmpty(deptList)){
            return new ArrayList<>();
        }
        List<AuthDeptTree> deptTreeList = new ArrayList<>();
        deptList.forEach(item -> {
            AuthDeptTree deptTree = new AuthDeptTree();
            BeanUtils.copyProperties(item,deptTree);
            deptTreeList.add(deptTree);
        });
        return deptTreeList;
    }

    public List<TreeData> copyDeptToTreeData(List<AuthDept> deptList){
        if(CollectionUtils.isEmpty(deptList)){
            return new ArrayList<>();
        }
        List<TreeData> treeDataList = new ArrayList<>();
        deptList.forEach(item -> {
            TreeData treeData = new TreeData();
            treeData.setChecked(false);
            treeData.setExpand(false);
            treeData.setSelected(false);
            treeData.setTitle(item.getDeptName());
            treeData.setValue(item.getDeptId());
            treeDataList.add(treeData);
        });
        return treeDataList;
    }


}

