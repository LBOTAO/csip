package com.fsmer.csip.repository;

import com.fsmer.csip.entity.AuthRoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AuthRoleMenuRepository extends JpaRepository<AuthRoleMenu, Integer>, JpaSpecificationExecutor<AuthRoleMenu> {

    List<AuthRoleMenu> findByRoleId(String roleId);
}
