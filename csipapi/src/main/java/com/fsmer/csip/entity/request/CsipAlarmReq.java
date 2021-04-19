package com.fsmer.csip.entity.request;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CsipAlarmReq {
    private Integer pageNum;
    private Integer pageSize;
    private Integer eventType;
    private String startTime;
    private String endTime;
    private String alarmLocation;
    private Integer handleState;
    private Timestamp alarmTime;
}
