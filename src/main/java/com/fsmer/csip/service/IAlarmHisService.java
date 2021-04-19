package com.fsmer.csip.service;

import com.fsmer.csip.entity.AuthUser;
import com.fsmer.csip.entity.CsipAlarmHis;
import com.fsmer.csip.entity.request.CsipAlarmReq;
import com.fsmer.csip.entity.response.ResponseWrapper;

public interface IAlarmHisService {
    /**
     * 处理告警
     * @param alarmId  告警id
     * @param handleOpinion 处理意见
     * @return
     */
    ResponseWrapper handleAlarmInfo(Integer alarmId, String handleOpinion, AuthUser authUser);

    /**
     * 根据alarmId告警历史信息
     *
     */
    CsipAlarmHis findByAlarmId(Integer alarmId);

    /**
     * 查询历史告警信息
     */
    ResponseWrapper findAlarmHisPage(CsipAlarmReq alarmReq);

    /**
     * 查询告警图片
     */
    ResponseWrapper getAlarmInfoHisByAlarmHisId(Integer alarmHisId);


    ResponseWrapper getAlarmIdByDetail(Integer alarmId);

    /**
     * 按照告警时间 （年月）  查询告警信息
     */
    public ResponseWrapper findAlarmByAlarmTime(String alarmTime);

    /**
     * 查询各事件类型发生数量
     */
    public Integer countEventType(Integer eventType);

    /**
     * 查询各事件类型发生数量按时间段
     */
    public Integer getEventCountByDate(String markNum,Integer eventType);
}
