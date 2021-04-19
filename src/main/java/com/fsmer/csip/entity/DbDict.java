package com.fsmer.csip.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@Entity
public class DbDict {
    @Id
    private String dictId;
    /** 字典类别 */
    private String dictKind;
    /** 值编码 */
    private String dictKey;
    /** 值名称 */
    private String dictValue;
    /** 值描述 */
    private String dictDesc;
    /** 是否有效 */
    private String isValid;
    /** 排序号 */
    private Integer sortNum;
    /** 乐观锁 */
    private Integer revision;
    /** 创建时间 */
    private Timestamp createdTime;
    /** 更新时间 */
    private Timestamp updatedTime;
    /** 备注 */
    private String memo;
}
