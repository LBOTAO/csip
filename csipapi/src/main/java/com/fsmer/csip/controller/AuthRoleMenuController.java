package com.fsmer.csip.controller;

import com.fsmer.csip.entity.AuthRoleMenu;
import com.fsmer.csip.entity.AuthUser;
import com.fsmer.csip.entity.TreeData;
import com.fsmer.csip.entity.response.AuthMenuTree;
import com.fsmer.csip.entity.response.ResponseWrapper;
import com.fsmer.csip.enumeration.ResponseCode;
import com.fsmer.csip.service.AuthRoleMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 角色菜单管理器类.
 */
@RestController
@RequestMapping("role")
@Api(tags = {"角色接口"})
public class AuthRoleMenuController {
    @Autowired
    AuthRoleMenuService authRoleMenuService;

    @GetMapping("getRoleMenuList")
    @ApiOperation(value = "查询角色菜单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId",value = "角色id",dataType = "String",paramType = "query")
    })
    public ResponseWrapper<List<AuthRoleMenu>> getRoleMenuList(String roleId) {
        return ResponseWrapper.createBySuccessCodeMessage(ResponseCode.SUCCESS.getMsg(),authRoleMenuService.getRoleMenuList(roleId));
    }

    @PostMapping("updateRoleMenu")
    @ApiOperation(value = "更新角色菜单列表")
    public ResponseWrapper<List<AuthRoleMenu>> updateRoleMenu(@RequestBody  Map<String,Object> map, AuthUser authUser){
        return ResponseWrapper.createBySuccessCodeMessage(ResponseCode.SUCCESS.getMsg(),authRoleMenuService.updateRoleMenu(map,authUser));
    }

    @GetMapping("getRoleMenuTree")
    @ApiOperation(value = "查询角色菜单树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId",value = "角色id",dataType = "String",paramType = "query")
    })
    public ResponseWrapper<List<TreeData>> getRoleMenuTree(String roleId) {
        return ResponseWrapper.createBySuccessCodeMessage(ResponseCode.SUCCESS.getMsg(),authRoleMenuService.getRoleMenuTree(roleId));
    }

}
