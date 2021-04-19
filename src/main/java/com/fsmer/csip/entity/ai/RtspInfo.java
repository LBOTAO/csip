package com.fsmer.csip.entity.ai;

import lombok.Data;

/**
 * @program: csipapi
 * @description:
 * @author: Tracy
 * @create: 2021-03-15 10:52
 */

@Data
public class RtspInfo {
    private int area_type;

    private String camera_id;

    private String start_time;

    private String status;
}
