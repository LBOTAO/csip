package com.fsmer.csip.controller;

import com.fsmer.csip.annotation.CurrentUser;
import com.fsmer.csip.entity.AuthUser;
import com.fsmer.csip.entity.request.CsipAlarmReq;
import com.fsmer.csip.entity.response.ResponseWrapper;
import com.fsmer.csip.entity.vo.EchartsPieChartVO;
import com.fsmer.csip.service.IAlarmHisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("alarmHisInfo")
@Api(tags = {"历史告警信息"})
public class CsipAlarmHisController {
    @Autowired
    private IAlarmHisService alarmHisService;

    @ApiOperation(value = "处理告警信息")
    @PostMapping("handleAlarmInfo")
    public ResponseWrapper handleAlarmInfo(@RequestParam("alarmId") Integer alarmId,
                                           @RequestParam("handleOpinion") String handleOpinion,
                                           @CurrentUser AuthUser authUser){
        return alarmHisService.handleAlarmInfo(alarmId,handleOpinion,authUser);
    }

    @ApiOperation(value = "查询历史告警记录")
    @PostMapping("findAlarmHisPage")
    public ResponseWrapper findAlarmHisPage(@RequestBody CsipAlarmReq req){
        return alarmHisService.findAlarmHisPage(req);
    }

    @ApiOperation(value = "查询历史告警详情")
    @PostMapping("getAlarmInfoHisByAlarmHisId")
    public ResponseWrapper getAlarmInfoHisByAlarmHisId(){
        return null;
    }



    @ApiOperation(value = "查询详情")
    @GetMapping("getAlarmIdByDetail")
    public ResponseWrapper getAlarmIdByDetail(Integer alarmId){
        return alarmHisService.getAlarmIdByDetail(alarmId);
    }

    @ApiOperation(value = "按照告警年月查询告警信息")
    @GetMapping("findAlarmByAlarmTime")
    public ResponseWrapper findAlarmByAlarmTime(String alarmTime){
        return alarmHisService.findAlarmByAlarmTime(alarmTime);
    }

    @ApiOperation(value = "查询各事件类型发生数量")
    @GetMapping("countEventType")
    public ResponseWrapper countEventType(){
        List<EchartsPieChartVO> results=new ArrayList<>();
        EchartsPieChartVO echart = new EchartsPieChartVO();
        EchartsPieChartVO echart2 = new EchartsPieChartVO();
        Integer eventTypesg = alarmHisService.countEventType(0);
        Integer eventTyperq = alarmHisService.countEventType(1);
        echart.setName("施工作业");
        echart.setValue(eventTypesg);
        echart2.setName("人员入侵");
        echart2.setValue(eventTyperq);
        results.add(echart);
        results.add(echart2);
        return ResponseWrapper.createBySuccess(results);
    }

    @ApiOperation(value = "查询各事件类型发生数量按照时间段")
    @GetMapping("countEventTypeByDate")
    public ResponseWrapper countEventTypeByDate(String markNum){
        List<EchartsPieChartVO> results=new ArrayList<>();
        EchartsPieChartVO echart = new EchartsPieChartVO();
        EchartsPieChartVO echart2 = new EchartsPieChartVO();
        Integer eventTypesg = alarmHisService.getEventCountByDate(markNum, 0);
        Integer eventTyperq = alarmHisService.getEventCountByDate(markNum, 1);
        echart.setName("施工作业");
        echart.setValue(eventTypesg);
        echart2.setName("人员入侵");
        echart2.setValue(eventTyperq);
        results.add(echart);
        results.add(echart2);
        return ResponseWrapper.createBySuccess(results);
    }
}
