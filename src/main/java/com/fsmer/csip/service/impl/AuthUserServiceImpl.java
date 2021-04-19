package com.fsmer.csip.service.impl;

import com.fsmer.csip.entity.*;
import com.fsmer.csip.entity.response.ResponseWrapper;
import com.fsmer.csip.entity.response.login.AccessToken;
import com.fsmer.csip.entity.response.user.UserInfoPage;
import com.fsmer.csip.enumeration.ResponseCode;
import com.fsmer.csip.repository.AuthDeptRepository;
import com.fsmer.csip.repository.AuthMenuRepository;
import com.fsmer.csip.repository.AuthUserRepository;
import com.fsmer.csip.service.AuthRoleService;
import com.fsmer.csip.service.AuthUserRoleService;
import com.fsmer.csip.service.AuthUserService;
import com.fsmer.csip.util.JwtUtil;
import com.fsmer.csip.util.MD5;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class AuthUserServiceImpl implements AuthUserService {
    @Autowired
    private AuthUserRepository authUserRepository;
    @Autowired
    private AuthMenuRepository authMenuRepository;
    @Autowired
    private AuthRoleService authRoleService;
    @Autowired
    private AuthDeptRepository authDeptRepository;
    @Autowired
    private AuthUserRoleService authUserRoleService;
/*    public static Map<String, AuthUser> USERINFO_MAP = new HashMap<>();*/

    @Override
    public AccessToken login(String loginName, String loginPassword) {
        AuthUser authUser = authUserRepository.findByLoginNameAndLoginPasswordAndIsValid(loginName, MD5.md5Password(loginPassword),"1");
        if (!Optional.ofNullable(authUser).isPresent()) return null;
        String token = JwtUtil.sign(authUser.getUserId());
        if (StringUtils.isEmpty(token)) return null;
        List<AuthRole> authRoleList = authRoleService.findByUserId(authUser.getUserId());
        String[] access = authRoleList.stream().map(AuthRole::getRoleId).collect(Collectors.toList()).toArray(new String[authRoleList.size()]);
        authUser.setAccess(access);
/*        USERINFO_MAP.put(token, authUser);*/
        return new AccessToken(token, JwtUtil.EXPIRE_TIME, authUser,access);
    }

    @Override
    public AuthUser getUserInfo(AuthUser authUser) {
        List<AuthRole> authRoleList = authRoleService.findByUserId(authUser.getUserId());
        if (!CollectionUtils.isEmpty(authRoleList)) {
            authUser.setAuthRoleList(authRoleList);
            String[] access = authRoleList.stream().map(AuthRole::getRoleId).collect(Collectors.joining(",")).split(",");
            authUser.setAccess(access);
            /*List<AuthMenu> authMenuList = authMenuRepository.findByRoleIds(roleIds);
            if (!CollectionUtils.isEmpty(authMenuList)) {
                userInfo.setAuthMenuList(authMenuList);
                Object[] access = authMenuList.stream().map(AuthMenu:: getMenuId).toArray();
                userInfo.setAccess(access);
            }*/
        }
        return authUser;
    }

    /**
     * 获取人员分页列表
     * @param authUser 查询条件
     * @param currentUser 当前用户
     * @return
     */
    @Override
    public Page<UserInfoPage> getUserListAsPage(AuthUser authUser, AuthUser currentUser){
        Integer page = authUser.getPageNum();
        Integer pageSize = authUser.getPageSize();
        String userName = authUser.getUserName();
        Pageable pageable = PageRequest.of(page-1,
                pageSize);
        AuthUser sample = new AuthUser();
        if (StringUtils.isNotEmpty(userName)) sample.setUserName(userName);
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withMatcher("userName", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<AuthUser> example = Example.of(sample, matcher);
        Page<AuthUser> userPage = authUserRepository.findAll(example, pageable);
        for(int i=0;i<userPage.getContent().size();i++){
            transforName(userPage,i);
        }
        List<UserInfoPage> userVOList = new ArrayList<>();
        userPage.getContent().forEach(item -> {
            UserInfoPage userVO = new UserInfoPage();
            BeanUtils.copyProperties(item, userVO);
            userVO.setAuthRoleList(authRoleService.findByUserId(userVO.getUserId()));
            userVOList.add(userVO);
        });

        Page<UserInfoPage> userInfoPage = new PageImpl<>(
                userVOList,
                PageRequest.of(userPage.getNumber(), userPage.getSize()), userPage.getTotalElements());
        return userInfoPage;
    }

    /**
     * 新增
     * @param user 对象
     * @param user 当前用户
     * @return
     */
    @Override
    public AuthUser insertAuthUser(AuthUser user, AuthUser currentUser){
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        String userId = currentUser.getUserId();
        AuthUser savedAuthUser = new AuthUser();
        String newUserId = UUID.randomUUID().toString();
        user.setLoginPassword(MD5.md5Password(user.getLoginPassword()));
        BeanUtils.copyProperties(user, savedAuthUser);
        savedAuthUser.setCreatedBy(userId);
        savedAuthUser.setCreatedTime(currentTime);
        savedAuthUser.setUpdatedBy(userId);
        savedAuthUser.setUpdatedTime(currentTime);
        savedAuthUser.setUserId(newUserId);
        savedAuthUser.setRevision(0);
        authUserRepository.save(savedAuthUser);
        //保存角色
        insertUserRole(newUserId,user.getRoleId(),currentUser);
        return savedAuthUser ;
    }

    /**
     * 更新
     * @param authUser 对象
     * @param authUser 当前用户
     * @return
     */
    @Override
    public ResponseWrapper<AuthUser> updateAuthUser(AuthUser authUser, AuthUser currentUser){
        Optional<AuthUser> userOptional = authUserRepository.findById(authUser.getUserId());
        if (!userOptional.isPresent()){
            return ResponseWrapper
                    .createBySuccessCodeMessage(ResponseCode.OBJ_ON_EXISTENT.getMsg(),null);
        }
        AuthUser updateAuthUser = userOptional.get();
        if(!updateAuthUser.getUserId().equals(authUser.getUserId())){
            return ResponseWrapper.createBySuccessCodeMessage(ResponseCode.OBJ_ON_EXISTENT.getMsg(),updateAuthUser);
        }
        if(authUser.getLoginPassword() != null){
            updateAuthUser.setLoginPassword(MD5.md5Password(authUser.getLoginPassword()));
        }else{
            authUser.setLoginPassword(updateAuthUser.getLoginPassword());
        }
        BeanUtils.copyProperties(authUser, updateAuthUser);
        updateAuthUser.setUpdatedBy(currentUser.getUserId());
        updateAuthUser.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
        authUserRepository.save(updateAuthUser);
        //保存角色
        insertUserRole(authUser.getUserId(),authUser.getRoleId(),currentUser);
        return ResponseWrapper.createBySuccessCodeMessage(ResponseCode.SUCCESS.getMsg(),updateAuthUser);
    }

    /**
     * 删除信息
     * @param userIds ids
     * @return
     */
    @Override
    public ResponseWrapper<List<AuthUser>> deleteAuthUser(String userIds){
        if(StringUtils.isEmpty(userIds)){
            return ResponseWrapper.createByErrorCodeMessage(ResponseCode.OBJ_ON_EXISTENT.getCode(),"对象不存在");
        }
        List<AuthUser> userList;
        try{
            String[] userIdArr = userIds.split(",");
            userList = authUserRepository.findAllById(Arrays.asList(userIdArr));
            authUserRepository.deleteAll(userList);
            authUserRoleService.deleteUserRoleByUserIds(userIds);
        }catch(Exception e){
            return ResponseWrapper.createByErrorCodeMessage(ResponseCode.ERROR.getCode(),"删除失败");
        }
        return ResponseWrapper.createBySuccessCodeMessage("删除成功",userList);
    }


    @Override
    public ResponseWrapper<List<AuthUserRole>> insertUserRole(String userIds,List<String> roleIds, AuthUser currentUser){
        if(CollectionUtils.isEmpty(roleIds)){
            return null;
        }
        String[] userIdArr = userIds.split(",");
        //先删除
        authUserRoleService.deleteUserRoleByUserIds(userIds);
        //再新增
        ResponseWrapper<List<AuthUserRole>> userRoleList = authUserRoleService.insertUserRole(userIdArr,roleIds,currentUser);
        return userRoleList;
    }

    private void transforName(Page<AuthUser> userPage, int i){
        AuthUser authUser = userPage.getContent().get(i);
        String createdName = "";
        String updatedName = "";
        String deptName = "";
        if(authUser.getCreatedBy() != null){
            Optional<AuthUser> createdUser = authUserRepository.findById(authUser.getCreatedBy());
            createdName = createdUser.isPresent() ? createdUser.get().getUserName() : "";
        }
        if(authUser.getUpdatedBy() != null){
            Optional<AuthUser> updatedUser = authUserRepository.findById(authUser.getUpdatedBy());
            updatedName = updatedUser.isPresent() ? updatedUser.get().getUserName() : "";
        }
        if(authUser.getDeptId() != null){
            Optional<AuthDept> dept = authDeptRepository.findById(authUser.getDeptId());
            deptName = dept.isPresent() ? dept.get().getDeptName() : "";
        }
        userPage.getContent().get(i).setCreatedByName(createdName);
        userPage.getContent().get(i).setUpdatedByName(updatedName);
        userPage.getContent().get(i).setDeptName(deptName);
    }

}
