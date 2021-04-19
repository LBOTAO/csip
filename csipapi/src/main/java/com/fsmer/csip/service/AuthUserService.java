package com.fsmer.csip.service;

import com.fsmer.csip.entity.AuthUser;
import com.fsmer.csip.entity.AuthUserRole;
import com.fsmer.csip.entity.response.ResponseWrapper;
import com.fsmer.csip.entity.response.login.AccessToken;
import com.fsmer.csip.entity.response.user.UserInfoPage;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AuthUserService {
    AccessToken login(String loginName, String loginPassword);
    AuthUser getUserInfo(AuthUser authUser);
    /**
     * 获取人员分页列表
     * @param authUser 查询条件
     * @param authUser 当前用户
     * @return
     */
    Page<UserInfoPage> getUserListAsPage(AuthUser authUser, AuthUser currentUser);

    ResponseWrapper<AuthUser> updateAuthUser(AuthUser authUser, AuthUser currentUser);

    AuthUser insertAuthUser(AuthUser authUser,AuthUser currentUser);

    ResponseWrapper<List<AuthUser>> deleteAuthUser(String userIds);

    ResponseWrapper<List<AuthUserRole>> insertUserRole(String userIds,List<String> roleIds, AuthUser currentUser);
}
