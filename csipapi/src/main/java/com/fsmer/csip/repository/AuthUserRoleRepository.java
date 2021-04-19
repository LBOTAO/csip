package com.fsmer.csip.repository;

import com.fsmer.csip.entity.AuthUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthUserRoleRepository extends JpaRepository<AuthUserRole, Integer> {

    List<AuthUserRole> findByUserIdIn(String[] userIds);

    List<AuthUserRole> findByUserId(String userId);
}
