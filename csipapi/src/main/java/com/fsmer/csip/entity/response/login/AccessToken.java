package com.fsmer.csip.entity.response.login;

import com.fsmer.csip.entity.AuthUser;
import com.fsmer.csip.entity.vo.RoleVO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AccessToken {
    private String accessToken;
    private Long expiresIn;
    private AuthUser userInfo;
    private String[] access;
}
