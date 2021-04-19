package com.fsmer.csip.service.impl;

import com.fsmer.csip.entity.AuthUser;
import com.fsmer.csip.entity.AuthUserRole;
import com.fsmer.csip.entity.response.ResponseWrapper;
import com.fsmer.csip.repository.AuthUserRoleRepository;
import com.fsmer.csip.service.AuthUserRoleService;
import com.fsmer.csip.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthUserRoleServiceImpl implements AuthUserRoleService {
    @Autowired
    private AuthUserRoleRepository authUserRoleRepository;

    @Override
    public void deleteUserRoleByUserIds(String userIds){
        String[] userIdArr = userIds.split(",");
        List<AuthUserRole> roleList = authUserRoleRepository.findByUserIdIn(userIdArr);
        authUserRoleRepository.deleteAll(roleList);
    }

    @Override
    public ResponseWrapper<List<AuthUserRole>> insertUserRole(String[] userIds, List<String> roleIds, AuthUser currentUser){
        try{
            List<AuthUserRole> newRoleList = new ArrayList<>();
            for(String userId : userIds){
                for(String roleId : roleIds){
                    AuthUserRole userRole = new AuthUserRole();
                    userRole.setUserId(userId);
                    userRole.setRoleId(roleId);
                    userRole.setCreatedBy(currentUser.getUserId());
                    newRoleList.add(userRole);
                }
            }
            List<AuthUserRole> result = authUserRoleRepository.saveAll(newRoleList);
            return ResponseWrapper.createBySuccessCodeMessage("保存成功",result);
        }catch (Exception e){
            LogUtil.getBussinessLogger().error(e.getMessage());
            return ResponseWrapper.createByErrorMessage("保存失败");
        }
    }

    @Override
    public List<String> findUserRolesByUserId(String userId){
        List<AuthUserRole> userRoleList = authUserRoleRepository.findByUserId(userId);
        if(CollectionUtils.isEmpty(userRoleList)){
            return null;
        }
        List<String> roleIds = userRoleList.stream().map(AuthUserRole :: getRoleId).collect(Collectors.toList());
        return roleIds;
    }
}
