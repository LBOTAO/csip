package com.fsmer.csip.controller;

import com.fsmer.csip.entity.response.ResponseWrapper;
import com.fsmer.csip.entity.vo.AlarmVO;
import com.fsmer.csip.service.IAlarmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: csipapi
 * @description: 报警信息
 * @author: Tracy
 * @create: 2021-01-25 15:45
 */
@RestController
@RequestMapping("callback")
@Api(tags = {"报警信息【李宪娜】"})
public class CallbackController {

    @Autowired
    private IAlarmService alarmService;
    @ApiOperation(value = "添加报警信息")
    @PostMapping("saveAlarm")
    public ResponseWrapper saveAlarm(@RequestBody AlarmVO vo) {
        return ResponseWrapper.createBySuccess(alarmService.saveAlarm(vo));
    }
    @ApiOperation(value = "test")
    @GetMapping("findById")
    public ResponseWrapper findById(Integer id) {
        return ResponseWrapper.createBySuccess(id);
    }


}
