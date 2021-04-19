package com.fsmer.csip.controller;

import com.fsmer.csip.annotation.CurrentUser;
import com.fsmer.csip.entity.AuthUser;
import com.fsmer.csip.entity.request.UserLoginReq;
import com.fsmer.csip.entity.response.ResponseWrapper;
import com.fsmer.csip.entity.response.login.AccessToken;
import com.fsmer.csip.enumeration.ResponseCode;
import com.fsmer.csip.service.AuthUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Api(tags = {"用户登录接口"})
public class LoginController {
    @Autowired
    private AuthUserService authUserService;

    @GetMapping("getUserInfo")
    @ApiOperation(value = "根据token获取用户信息")
    public ResponseWrapper<AuthUser> getUserInfo(@CurrentUser AuthUser authUser) {
        return ResponseWrapper.createBySuccessCodeMessage("success", authUserService.getUserInfo(authUser));
    }

    @PostMapping("login")
    @ApiOperation(value = "登录接口")
    public ResponseWrapper<AccessToken> login(@RequestBody UserLoginReq user) {
        AccessToken accessToken = authUserService.login(user.getUsername(), user.getPassword());
        if (!Optional.ofNullable(accessToken).isPresent()) {
            return ResponseWrapper.createByErrorCodeMessage(
                    ResponseCode.LOGIN_FAILED.getCode(), ResponseCode.LOGIN_FAILED.getMsg());
        }
        return ResponseWrapper.createBySuccess(accessToken);
    }
}
