package com.fsmer.csip.controller;

import com.fsmer.csip.annotation.CurrentUser;
import com.fsmer.csip.entity.AuthDept;
import com.fsmer.csip.entity.AuthUser;
import com.fsmer.csip.entity.TreeData;
import com.fsmer.csip.entity.response.AuthDeptTree;
import com.fsmer.csip.entity.response.ResponseWrapper;
import com.fsmer.csip.service.AuthDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 组织机构管理器类.
 */
@RestController
@RequestMapping("dept")
@Api(tags = {"部门表（组织机构）"})
public class AuthDeptController {
    @Autowired
    AuthDeptService authDeptService;

    @GetMapping("getDeptList")
    @ApiOperation(value = "根据部门名称查询所有部门-树形结构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentDeptId",value = "父级id",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "deptName",value = "部门名称",dataType = "String",paramType = "query")
    })
    public ResponseWrapper<List<AuthDeptTree>> getDeptList(String deptName,String parentDeptId) {
        return ResponseWrapper.createBySuccessCodeMessage("successfully",authDeptService.getDeptTreeList(deptName,parentDeptId));
    }

    @PostMapping("insertAuthDept")
    @ApiOperation(value = "新增部门信息")
    public ResponseWrapper<AuthDept> insertAuthDept(@RequestBody AuthDept dept, @CurrentUser AuthUser authUser) {
        return ResponseWrapper.createBySuccessCodeMessage("保存成功",authDeptService.insertAuthDept(dept,authUser));
    }

    @PostMapping("updateAuthDept")
    @ApiOperation(value = "更新部门信息")
    public ResponseWrapper<AuthDept> updateAuthDept(@RequestBody AuthDept dept, @CurrentUser AuthUser authUser) {
        return ResponseWrapper.createBySuccessCodeMessage("更新成功",authDeptService.updateAuthDept(dept,authUser));
    }

    @PostMapping("deleteAuthDept")
    @ApiOperation(value = "删除部门信息，包括批量")
    @ApiImplicitParam(name = "deptIds",value = "部门id，多个用逗号隔开",dataType = "String",paramType = "query")
    public ResponseWrapper<List<AuthDept>> deleteAuthDept(String deptIds) {
        return authDeptService.deleteAuthDept(deptIds);
    }

    @GetMapping("getDeptTree")
    @ApiOperation(value = "根据父级节点查询所有部门-树形结构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentDeptId",value = "父级id",dataType = "String",paramType = "query"),
    })
    public ResponseWrapper<List<TreeData>> getDeptTree(String parentDeptId) {
        return ResponseWrapper.createBySuccessCodeMessage("successfully",authDeptService.getDeptTree(parentDeptId));
    }

}
