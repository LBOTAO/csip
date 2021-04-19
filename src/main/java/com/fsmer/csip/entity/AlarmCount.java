package com.fsmer.csip.entity;

import lombok.Data;

import javax.persistence.Transient;

@Data
public class AlarmCount {
    private String cameraId;
    private Long alarmNum;

    public AlarmCount(String cameraId, Long alarmNum) {
        this.cameraId = cameraId;
        this.alarmNum = alarmNum;
    }
}
