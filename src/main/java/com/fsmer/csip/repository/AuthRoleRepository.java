package com.fsmer.csip.repository;

import com.fsmer.csip.entity.AuthRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthRoleRepository extends JpaRepository<AuthRole, String>, JpaSpecificationExecutor<AuthRole> {

    List<AuthRole> findByRoleNameLike(String roleName);

    List<AuthRole> findByParentRoleId(String parentRoleId);

    List<AuthRole> findByIsDefault(String s);
}
