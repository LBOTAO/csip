package com.fsmer.csip.entity;

import lombok.Data;

import java.util.List;

@Data
public class TreeData {
    private String title;
    private boolean expand;
    private String value;
    private boolean selected;
    private boolean checked;
    private Integer sortNum;
    private boolean disabled;
    private List<TreeData> children;

}
