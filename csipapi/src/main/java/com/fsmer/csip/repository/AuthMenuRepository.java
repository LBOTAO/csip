package com.fsmer.csip.repository;

import com.fsmer.csip.entity.AuthMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthMenuRepository extends JpaRepository<AuthMenu, String>, JpaSpecificationExecutor<AuthMenu> {
    @Query(value = "select p.* from auth_menu p, auth_role_menu rrp " +
            "where p.menu_id = rrp.menu_id " +
            "and rrp.role_id in (:roleIds) " +
            "order by p.sort_num",
            nativeQuery = true)
    List<AuthMenu> findByRoleIds(List<String> roleIds);

    List<AuthMenu> findByMenuNameLike(String s);

    List<AuthMenu> findByParentMenuId(String parentMenuId);
}
