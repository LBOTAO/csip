package com.fsmer.csip.controller;

import com.fsmer.csip.annotation.CurrentUser;
import com.fsmer.csip.entity.AuthRole;
import com.fsmer.csip.entity.AuthUser;
import com.fsmer.csip.entity.response.ResponseWrapper;
import com.fsmer.csip.entity.response.user.UserInfoPage;
import com.fsmer.csip.repository.AuthUserRepository;
import com.fsmer.csip.service.AuthRoleService;
import com.fsmer.csip.service.AuthUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Api(tags = {"用户管理"})
@Transactional
public class AuthUserController {
    @Autowired
    private AuthUserRepository authUserRepository;
    @Autowired
    private AuthRoleService authRoleService;
    @Autowired
    private AuthUserService authUserService;

    @PostMapping("/getUserList")
    @ApiOperation("用户列表查询")
    public ResponseWrapper<Page<UserInfoPage>> getUserList(@RequestBody AuthUser user,@CurrentUser AuthUser authUser) {
        Page<UserInfoPage> userVOPage = authUserService.getUserListAsPage(user,authUser);
        return ResponseWrapper
                .createBySuccessCodeMessage("successfully", userVOPage);
    }

    @PostMapping("/insertUser")
    @ApiOperation("添加用户")
    public ResponseWrapper<AuthUser> insertUser(@RequestBody AuthUser newUser, @CurrentUser AuthUser authUser) {
        return ResponseWrapper
                .createBySuccessCodeMessage("successfully", authUserService.insertAuthUser(newUser,authUser));
    }

    @PostMapping("/updateUser")
    @ApiOperation("更新用户")
    public ResponseWrapper<AuthUser> updateUser(@RequestBody AuthUser updateUser, @CurrentUser AuthUser authUser) {
        return authUserService.updateAuthUser(updateUser,authUser);
    }

    @GetMapping("/getUserById")
    @ApiOperation("根据id查询用户")
    public ResponseWrapper<AuthUser> getUserById(String userId, @CurrentUser AuthUser authUser) {
        if (StringUtils.isEmpty(userId)) {
            return ResponseWrapper.createByError();
        }
        Optional<AuthUser> optionalUser = authUserRepository.findById(userId);
        if(!optionalUser.isPresent()){
            return ResponseWrapper.createBySuccessCodeMessage("用户不存在", authUser);
        }
        AuthUser newAuthUser = optionalUser.get();
        List<AuthRole> authRoleList = authRoleService.findByUserId(newAuthUser.getUserId());
        if(!CollectionUtils.isEmpty(authRoleList)){
            List<String> roleId = authRoleList.stream().map(AuthRole::getRoleId).collect(Collectors.toList());
            newAuthUser.setRoleId(roleId);
        }
        return ResponseWrapper.createBySuccessCodeMessage("successfully", newAuthUser);
    }

    @GetMapping("/getUserInfo")
    @ApiOperation("根据token查询用户信息")
    public ResponseWrapper<AuthUser> getUserInfo(@CurrentUser AuthUser authUser) {
        Optional<AuthUser> optionalUser = authUserRepository.findById(authUser.getUserId());
        if(!optionalUser.isPresent()){
            return ResponseWrapper.createBySuccessCodeMessage("用户不存在", authUser);
        }
        AuthUser newAuthUser = optionalUser.get();
        List<AuthRole> authRoleList = authRoleService.findByUserId(newAuthUser.getUserId());
        if(!CollectionUtils.isEmpty(authRoleList)){
            newAuthUser.setAuthRoleList(authRoleList);
        }
        return ResponseWrapper.createBySuccessCodeMessage("successfully", newAuthUser);
    }

    @PostMapping("/deleteUser")
    @ApiOperation("删除用户")
    @ApiImplicitParam(name = "userIds",value = "用户ids，多个用逗号隔开",type = "String",paramType = "query")
    public ResponseWrapper<List<AuthUser>> deleteUser(String userIds){
        return authUserService.deleteAuthUser(userIds);
    }

    @GetMapping("/checkLoginName")
    @ApiOperation("判断登录名是否唯一")
    public ResponseWrapper<List<AuthUser>> checkLoginName(String loginName){
        List<AuthUser> authUserList = authUserRepository.findByLoginName(loginName);
        String checkMsg = "many";
        if (CollectionUtils.isEmpty(authUserList)) {
            checkMsg = "one";
        }
        return ResponseWrapper.createBySuccessCodeMessage(checkMsg, authUserList);
    }

}
