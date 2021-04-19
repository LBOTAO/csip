package com.fsmer.csip.entity.response;

import com.fsmer.csip.enumeration.ResponseCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description = "通用返回类")
public class ResponseWrapper<T> implements Serializable {
    @ApiModelProperty(value = "状态")
    private final int status;
    @ApiModelProperty(value = "消息")
    private String msg;
    @ApiModelProperty(value = "返回对象")
    private T data;

    private ResponseWrapper(int status) {
        this.status = status;
    }

    private ResponseWrapper(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private ResponseWrapper(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ResponseWrapper(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public static <T> ResponseWrapper<T> createBySuccess() {
        return new ResponseWrapper<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ResponseWrapper<T> createBySuccess(T data) {
        return new ResponseWrapper<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ResponseWrapper<T> createBySuccessMessage(String msg) {
        return new ResponseWrapper<T>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ResponseWrapper<T> createBySuccessCodeMessage(String msg, T data) {
        return new ResponseWrapper<T>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> ResponseWrapper<T> createByError() {
        return new ResponseWrapper<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMsg());
    }

    public static <T> ResponseWrapper<T> createByErrorMessage(String errorMessage) {
        return new ResponseWrapper<T>(ResponseCode.ERROR.getCode(), errorMessage);
    }

    public static <T> ResponseWrapper<T> createByErrorCodeMessage(int erroCode, String errorMessage) {
        return new ResponseWrapper<T>(erroCode, errorMessage);
    }
}
