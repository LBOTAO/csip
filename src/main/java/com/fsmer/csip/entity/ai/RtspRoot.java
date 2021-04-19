package com.fsmer.csip.entity.ai;

import lombok.Data;

/**
 * @program: csipapi
 * @description:
 * @author: Tracy
 * @create: 2021-03-15 10:53
 */
@Data
public class RtspRoot {
    private int code;

    private RtspData data;

    private String msg;
}
