package com.fsmer.csip.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
public class CsipCameraInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String cameraId;
    private String cameraUrl;
    private String coverUrl;
    private String cameraLocation;
    private Integer cameraType;
    private String isOnline;
    private String isValid;
    private Integer sortNum;
    private Integer revision;
    private String longitude;
    private String latitude;
    private String createdBy;
    private String createdByName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp createdTime;
    private String updatedBy;
    private String updatedByName;
    private Timestamp updatedTime;
    private String memo;
    @Transient
    private Integer countEventsg;
    @Transient
    private Integer countEventrq;
    @Transient
    private Integer notHandleNum;

    @Transient
    private String checkStatus;
}
