package com.fsmer.csip.controller;

import com.fsmer.csip.annotation.CurrentUser;
import com.fsmer.csip.entity.AuthRole;
import com.fsmer.csip.entity.AuthUser;
import com.fsmer.csip.entity.TreeData;
import com.fsmer.csip.entity.response.AuthRoleTree;
import com.fsmer.csip.entity.response.ResponseWrapper;
import com.fsmer.csip.service.AuthRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理器类.
 */
@RestController
@RequestMapping("role")
@Api(tags = {"角色接口"})
public class AuthRoleController {
    @Autowired
    AuthRoleService authRoleService;

    @GetMapping("getRoleList")
    @ApiOperation(value = "查询角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentRoleId",value = "父级id",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "roleName",value = "名称",dataType = "String",paramType = "query")
    })
    public ResponseWrapper<List<AuthRoleTree>> getRoleList(String roleName, String parentRoleId) {
        return ResponseWrapper.createBySuccessCodeMessage("successfully",authRoleService.getRoleTreeList(roleName,parentRoleId));
    }

    @PostMapping("insertAuthRole")
    @ApiOperation(value = "新增角色信息")
    public ResponseWrapper<AuthRole> insertAuthRole(@RequestBody AuthRole role, @CurrentUser AuthUser authUser) {
        return ResponseWrapper.createBySuccessCodeMessage("保存成功",authRoleService.insertAuthRole(role,authUser));
    }

    @PostMapping("updateAuthRole")
    @ApiOperation(value = "更新角色信息")
    public ResponseWrapper<AuthRole> updateAuthRole(@RequestBody AuthRole role, @CurrentUser AuthUser authUser) {
        return ResponseWrapper.createBySuccessCodeMessage("更新成功",authRoleService.updateAuthRole(role,authUser));
    }

    @PostMapping("deleteAuthRole")
    @ApiOperation(value = "删除角色信息，包括批量")
    @ApiImplicitParam(name = "roleIds",value = "角色id，多个用逗号隔开",dataType = "String",paramType = "query")
    public ResponseWrapper<List<AuthRole>> deleteAuthRole(String roleIds) {
        return authRoleService.deleteAuthRole(roleIds);
    }

    @GetMapping("getRoleTree")
    @ApiOperation(value = "根据父级节点查询所有角色-树形结构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentRoleId",value = "父级id",dataType = "String",paramType = "query"),
    })
    public ResponseWrapper<List<TreeData>> getRoleTree(String parentRoleId) {
        return ResponseWrapper.createBySuccessCodeMessage("successfully",authRoleService.getRoleTree(parentRoleId));
    }

    @GetMapping("getDefaultRoleTree")
    @ApiOperation(value = "根据父级节点查询所有角色，默认角色默认勾选-树形结构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentRoleId",value = "父级id",dataType = "String",paramType = "query"),
    })
    public ResponseWrapper<List<TreeData>> getDefaultRoleTree(String parentRoleId) {
        return ResponseWrapper.createBySuccessCodeMessage("successfully",authRoleService.getDefaultRoleTree(parentRoleId));
    }

    @GetMapping("getDefaultRole")
    @ApiOperation(value = "查询所有默认角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentRoleId",value = "父级id",dataType = "String",paramType = "query"),
    })
    public ResponseWrapper<List<AuthRole>> getDefaultRole() {
        return ResponseWrapper.createBySuccessCodeMessage("successfully",authRoleService.getDefaultRole());
    }

}
