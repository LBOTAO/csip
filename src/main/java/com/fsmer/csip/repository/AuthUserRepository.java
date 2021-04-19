package com.fsmer.csip.repository;

import com.fsmer.csip.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AuthUserRepository extends JpaRepository<AuthUser, String>, JpaSpecificationExecutor<AuthUser> {

    List<AuthUser> findByDeptId(String deptId);

    List<AuthUser> findByLoginName(String loginName);

    AuthUser findByLoginNameAndLoginPasswordAndIsValid(String loginName, String md5Password,String isValid);

}
