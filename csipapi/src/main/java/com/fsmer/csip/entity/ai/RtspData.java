package com.fsmer.csip.entity.ai;

import lombok.Data;

import java.util.List;

/**
 * @program: csipapi
 * @description:
 * @author: Tracy
 * @create: 2021-03-15 10:52
 */
@Data
public class RtspData {
    private List<RtspInfo> rtsp_info ;

    private int total_rtsp;
}
