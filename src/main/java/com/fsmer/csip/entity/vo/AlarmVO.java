package com.fsmer.csip.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: csipapi
 * @description:
 * @author: Tracy
 * @create: 2021-01-25 15:53
 */
@Data
public class AlarmVO {
    @ApiModelProperty("告警信息id")
    private String event_id;
    @ApiModelProperty("摄像头id")
    private String camera_id;
    @ApiModelProperty("告警位置")
    private String alarm_location;
    @ApiModelProperty("告警状态（1：开始告警，2、告警中 3：结束告警）")
    private Integer alarm_type;//alarm_status
    @ApiModelProperty("事件类型（0：施工作业，1：人员入侵）")
    private Integer event_type;
    @ApiModelProperty("告警时间")
    private Long alarm_time;
    @ApiModelProperty("报警图片")
    private String  img_url;
    @ApiModelProperty("扩展字段")
    private String alarm_info;  //json


}
