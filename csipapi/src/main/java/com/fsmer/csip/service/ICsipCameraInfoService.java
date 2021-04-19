package com.fsmer.csip.service;

import com.fsmer.csip.entity.AuthUser;
import com.fsmer.csip.entity.CsipCameraInfo;
import com.fsmer.csip.entity.request.CameraInfoImagesReq;
import com.fsmer.csip.entity.request.CsipCameraInfoReq;
import com.fsmer.csip.entity.response.ResponseWrapper;

import java.util.List;

public interface ICsipCameraInfoService {
    /**
     * 摄像头分页查询
     * @param cameraInfo
     * @return
     */
    public ResponseWrapper findCameraInfoPage(CsipCameraInfoReq cameraInfo);


    /**
     * 添加摄像头
     */
    public ResponseWrapper addCameraInfo(CsipCameraInfo cameraInfo, AuthUser authUser);

    /**
     * 删除摄像头信息
     */
    public ResponseWrapper delCamerInfo(Integer id);

    /**
     * 根据摄像头id查询具体信息
     */
    public ResponseWrapper findCameraInfoById(Integer id);

    /**
     * 抓拍封面图
     * @param req
     * @param authUser
     * @author: Tracy
     * @return
     */
    ResponseWrapper updateCover(CameraInfoImagesReq req,AuthUser authUser);

    /**
     * 区域分布图统计
     * @return
     */
    ResponseWrapper findCameraInfoAll();

}
