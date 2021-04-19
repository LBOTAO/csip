package com.fsmer.csip.service;

import com.fsmer.csip.entity.AuthUser;
import com.fsmer.csip.entity.AuthUserRole;
import com.fsmer.csip.entity.response.ResponseWrapper;

import java.util.List;

public interface AuthUserRoleService {

    void deleteUserRoleByUserIds(String userIds);

    ResponseWrapper<List<AuthUserRole>> insertUserRole(String[] userIds, List<String> roleIds, AuthUser currentUser);

    List<String> findUserRolesByUserId(String userId);
}
