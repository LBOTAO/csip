package com.fsmer.csip.controller;

import com.fsmer.csip.entity.DbDict;
import com.fsmer.csip.entity.response.ResponseWrapper;
import com.fsmer.csip.repository.DbDictRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dict")
@Api(tags = {"字典接口"})
public class DbDictController {
    @Autowired
    private DbDictRepository dbDictRepository;

    @GetMapping("/getDictListByKind")
    @ApiOperation("根据字典类别获取字典数据")
    public ResponseWrapper<List<DbDict>> getDictListByType(String dictKind) {
        List<DbDict> dbDictList;
        if(null != dictKind){
            dbDictList = dbDictRepository.findByDictKind(dictKind);
        }else{
            dbDictList = dbDictRepository.findAll();
        }
        return ResponseWrapper
                .createBySuccessCodeMessage("successfully", dbDictList);
    }

}
