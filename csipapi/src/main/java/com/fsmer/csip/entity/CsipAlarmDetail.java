package com.fsmer.csip.entity;

import com.fsmer.csip.entity.vo.AlarmVO;
import com.fsmer.csip.util.DateTimeUtil;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
public class CsipAlarmDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer alarmDetailId;
    private Integer alarmId;
    private String imgPath;
    private String imgName;
    private String remoteImgPath;
    private Integer alarmStatus;
    @Column(columnDefinition = "text")
    private String eventRecord;
    private Timestamp alarmTime;
    private Integer revision;
    private Timestamp createdTime;
    private String memo;

    public CsipAlarmDetail() {
    }
    public CsipAlarmDetail(Integer alarmId,String fileUrl,String imgName ) {
        this.alarmId=alarmId;
        this.imgPath=fileUrl;
        this.imgName=imgName;
    }
    public  CsipAlarmDetail(CsipAlarm po, AlarmVO vo){
        this.alarmId=po.getAlarmId();
        this.imgPath=vo.getImg_url();
        this.remoteImgPath="";
        this.alarmStatus=vo.getAlarm_type();
        this. eventRecord=vo.getAlarm_info();
        this.alarmTime=po.getAlarmTime();
        this.createdTime= DateTimeUtil.toDay();
    }
}
