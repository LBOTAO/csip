package com.fsmer.csip.controller;

import com.fsmer.csip.entity.request.CsipAlarmReq;
import com.fsmer.csip.entity.response.ResponseWrapper;
import com.fsmer.csip.service.IAlarmService;
import com.fsmer.csip.util.upload.Base64Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("alarminfo")
@Api(tags = {"报警信息管理"})
public class CsipAlarmController {

    @Autowired
    private IAlarmService alarmService;

    @ApiOperation(value = "查询报警信息")
    @PostMapping("findAlarmPage")
    public ResponseWrapper findAlarmPage(@RequestBody CsipAlarmReq alarmReq) {
        return ResponseWrapper.createBySuccess(alarmService.findAlarmPage(alarmReq));
    }

    @ApiOperation(value = "查询未处理的报警信息")
    @GetMapping("findAlarmInfoByNotHandle")
    public ResponseWrapper findAlarmInfoByNotHandle(){
        return ResponseWrapper.createBySuccess(alarmService.findAlarmInfoByNotHandle());
    }

    @ApiOperation(value = "报警信息离线数据导入")
    @PostMapping("alarmInfoOffLineDataImport")
    public ResponseWrapper AlarmInfoOffLineDataImport(@RequestParam("file") MultipartFile file){
        String fileUrl = Base64Utils.uploadFile(file, "alarmInfo");
       return alarmService.saveOffLineData(file,fileUrl);
    }

    @ApiOperation(value = "统计未处理告警数量")
    @GetMapping("findAlarmInfoCount")
    public ResponseWrapper findAlarmInfoCount(){
        return ResponseWrapper.createBySuccess(alarmService.findAlarmInfoCount());
    }
}
