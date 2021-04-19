package com.fsmer.csip.entity.request;

import lombok.Data;

@Data
public class CsipCameraInfoReq {
    private Integer pageNum;
    private Integer pageSize;
    private String cameraId;
    private Integer cameraType;
    private String cameraLocation;

}
