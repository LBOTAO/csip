package com.fsmer.csip.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fsmer.csip.util.DateTimeUtil;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
public class CsipAlarmHis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer alarmHisId;
    private String  eventId;
    private Integer alarmId;
    private String cameraId;
    private String alarmLocation;
    private Integer alarmStatus;
    private Integer eventType;
    private Timestamp alarmTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp alarmStartTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp alarmEndTime;
    private String handleState;
    private String handleOpinion;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp handleTime;
    private String handleBy;
    private String handleByName;
    private Integer isOffline;
    private Integer revision;
    private Timestamp createdTime;
    private String memo;

    public CsipAlarmHis(){};

    public CsipAlarmHis(String eventId, String cameraId, String alarmLocation, Integer eventType, Timestamp alarmTime,Integer alarmStatus) {
        this.eventId = eventId;
        this.cameraId = cameraId;
        this.alarmLocation = alarmLocation;
        this.eventType = eventType;
        this.alarmStartTime = alarmTime;
        this.alarmEndTime = alarmTime;
        this.alarmStatus=alarmStatus;
        this.handleState="1";
        this.createdTime= DateTimeUtil.toDay();
    }
}
