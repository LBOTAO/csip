package com.fsmer.csip.service;

import com.fsmer.csip.entity.request.CsipAlarmReq;
import com.fsmer.csip.entity.response.ResponseWrapper;
import com.fsmer.csip.entity.vo.AlarmVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @program: csipapi
 * @description:
 * @author: Tracy 报警模块
 * @create: 2021-01-25 16:10
 */
public interface IAlarmService {

    /**
     *添加报警信息
     * @param vo
     * @return
     */
    ResponseWrapper saveAlarm(AlarmVO vo);

    /**
     * 查询告警信息
     */
    ResponseWrapper findAlarmPage(CsipAlarmReq alarmReq);

    /**
     * 查询未处理的告警信息
     */
    ResponseWrapper findAlarmInfoByNotHandle();

    /**
     * 添加离线数据
     */
    public ResponseWrapper saveOffLineData(MultipartFile file,String fileUrl);

    /**
     * 统计未处理告警数量
     * @return
     */
    int findAlarmInfoCount();

}
