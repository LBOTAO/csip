package com.fsmer.csip.controller;

import com.fsmer.csip.annotation.CurrentUser;
import com.fsmer.csip.entity.AuthMenu;
import com.fsmer.csip.entity.AuthUser;
import com.fsmer.csip.entity.TreeData;
import com.fsmer.csip.entity.response.AuthMenuTree;
import com.fsmer.csip.entity.response.ResponseWrapper;
import com.fsmer.csip.service.AuthMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理器类.
 */
@RestController
@RequestMapping("menu")
@Api(tags = {"菜单接口"})
public class AuthMenuController {
    @Autowired
    AuthMenuService authMenuService;

    @GetMapping("getList")
    @ApiOperation(value = "查询列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentMenuId",value = "父级id",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "menuName",value = "名称",dataType = "String",paramType = "query")
    })
    public ResponseWrapper<List<AuthMenuTree>> getList(String menuName, String parentMenuId) {
        return ResponseWrapper.createBySuccessCodeMessage("successfully",authMenuService.getMenuTreeList(menuName,parentMenuId));
    }

    @PostMapping("insertEntity")
    @ApiOperation(value = "新增单个实体对象")
    public ResponseWrapper<AuthMenu> insertEntity(@RequestBody AuthMenu menu, @CurrentUser AuthUser authUser) {
        return ResponseWrapper.createBySuccessCodeMessage("保存成功",authMenuService.insertAuthMenu(menu,authUser));
    }

    @PostMapping("updateEntity")
    @ApiOperation(value = "更新单个实体对象")
    public ResponseWrapper<AuthMenu> updateEntity(@RequestBody AuthMenu menu, @CurrentUser AuthUser authUser) {
        return ResponseWrapper.createBySuccessCodeMessage("更新成功",authMenuService.updateAuthMenu(menu,authUser));
    }

    @PostMapping("deleteEntitys")
    @ApiOperation(value = "删除实体对象，包括批量")
    @ApiImplicitParam(name = "menuIds",value = "菜单id，多个用逗号隔开",dataType = "String",paramType = "query")
    public ResponseWrapper<List<AuthMenu>> deleteEntitys(String menuIds) {
        return authMenuService.deleteAuthMenu(menuIds);
    }

    @GetMapping("getEntityTree")
    @ApiOperation(value = "根据父级节点查询所有菜单-树形结构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentMenuId",value = "父级id",dataType = "String",paramType = "query"),
    })
    public ResponseWrapper<List<TreeData>> getEntityTree(String parentMenuId) {
        return ResponseWrapper.createBySuccessCodeMessage("successfully",authMenuService.getMenuTree(parentMenuId));
    }

}
