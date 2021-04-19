package com.fsmer.csip.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fsmer.csip.entity.vo.AlarmVO;
import com.fsmer.csip.enumeration.AlarmStatusEnum;
import com.fsmer.csip.util.DateTimeUtil;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@Entity
public class CsipAlarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer alarmId;
    private String eventId;
    private String cameraId;
    private String alarmLocation;
    private Integer alarmStatus;
    private Integer eventType;
    private Timestamp alarmTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp alarmStartTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp alarmEndTime;
    private Integer isOffline;
    private Integer revision;
    private Timestamp createdTime;
    private String memo;

    public CsipAlarm() {}

    public CsipAlarm(AlarmVO vo) {
        this.eventId=vo.getEvent_id();
        this.cameraId=vo.getCamera_id();
        this.alarmLocation=vo.getAlarm_location();
        this.alarmStatus=vo.getAlarm_type();
        this.eventType=vo.getEvent_type();
        this. alarmTime= DateTimeUtil.longToTimestamp(vo.getAlarm_time());
        if (vo.getAlarm_type()== AlarmStatusEnum.START.getId())
        this.alarmStartTime=DateTimeUtil.longToTimestamp(vo.getAlarm_time());
        if (vo.getAlarm_type()== AlarmStatusEnum.END.getId())
         this.alarmEndTime=DateTimeUtil.longToTimestamp(vo.getAlarm_time());
        this.isOffline=1;
        this.createdTime=DateTimeUtil.toDay();
    }

    public CsipAlarm(String eventId, String cameraId, String alarmLocation, Integer eventType, Timestamp alarmTime,Integer alarmStatus) {
        this.eventId = eventId;
        this.cameraId = cameraId;
        this.alarmLocation = alarmLocation;
        this.eventType = eventType;
        this.alarmTime = alarmTime;
        this.alarmStartTime = alarmTime;
        this.alarmEndTime = alarmTime;
        this.alarmStatus=alarmStatus;
        this.createdTime= DateTimeUtil.toDay();
    }
}
