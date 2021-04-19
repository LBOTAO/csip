package com.fsmer.csip.entity.request;

import lombok.Data;

/**
 * @program: csipapi
 * @description:
 * @author: Tracy
 * @create: 2021-02-05 10:48
 */
@Data
public class CameraInfoImagesReq {
    private Integer id;
    private String base64;
}
