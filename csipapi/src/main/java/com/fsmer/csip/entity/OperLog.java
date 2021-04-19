package com.fsmer.csip.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class OperLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer operId;
    /** 功能模块 */
    private String operModule;
    /** 操作类型 */
    private String operType;
    /** 描述 */
    private String operDesc;
    /** 请求参数 */
    @Column(columnDefinition = "Text")
    private String operRequestParams;
    /** 响应参数 */
    @Column(columnDefinition = "longtext")
    private String operResponseParams;
    /** 操作人id */
    private Integer operUserId;
    /** 操作人姓名 */
    private String operUserName;
    /** 操作方法 */
    private String operMethod;
    /** 操作uri */
    private String operUri;
    /** 操作ip */
    private String operIp;
    /** 操作时间 */
    private Date operTime;
    /** 相关编号 */
    private Integer operRelationId;
}
