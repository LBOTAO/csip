package com.fsmer.csip.repository;

import com.fsmer.csip.entity.AuthDept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AuthDeptRepository extends JpaRepository<AuthDept, String>, JpaSpecificationExecutor<AuthDept> {

    List<AuthDept> findByParentDeptId(String parentDeptId);

    List<AuthDept> findByDeptIdIn(String[] deptIds);

    List<AuthDept> findByDeptNameLike(String deptName);
}
