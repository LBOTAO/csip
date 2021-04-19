package com.fsmer.csip.controller;

import com.fsmer.csip.annotation.CurrentUser;
import com.fsmer.csip.entity.AuthUser;
import com.fsmer.csip.entity.CsipCameraInfo;
import com.fsmer.csip.entity.request.CameraInfoImagesReq;
import com.fsmer.csip.entity.request.CsipCameraInfoReq;
import com.fsmer.csip.entity.response.ResponseWrapper;
import com.fsmer.csip.service.ICsipCameraInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("camerinfo")
@Api(tags = {"摄像头信息"})
public class CsipCameraInfoController {
    @Autowired
    private ICsipCameraInfoService csipCameraInfoService;

    @PostMapping(value = "/findCameraInfoPage")
    public ResponseWrapper findCameraInfoPage(@RequestBody CsipCameraInfoReq cameraInfo) {
        return ResponseWrapper.createBySuccess(csipCameraInfoService.findCameraInfoPage(cameraInfo));
    }

    @DeleteMapping(value = "/delCamerInfoByCameraId")
    public ResponseWrapper delCamerInfo(@RequestParam("id")Integer id){
        return ResponseWrapper.createBySuccess(csipCameraInfoService.delCamerInfo(id));
    }

    @PostMapping(value = "/findCameraInfoById")
    public ResponseWrapper findCameraInfoById(@RequestParam("id")Integer id){
        return ResponseWrapper.createBySuccess(csipCameraInfoService.findCameraInfoById(id));
    }

    @PostMapping(value = "/addCameraInfo")
    public ResponseWrapper addCameraInfo(@RequestBody CsipCameraInfo cameraInfo, @CurrentUser AuthUser authUser){
        return ResponseWrapper.createBySuccess(csipCameraInfoService.addCameraInfo(cameraInfo,authUser));
    }


    @PostMapping(value = "/updateCover")
    @ApiOperation(value = "更新摄像头封面")
    public ResponseWrapper updateCover(@RequestBody CameraInfoImagesReq req, @CurrentUser AuthUser authUser){
        return ResponseWrapper.createBySuccess(csipCameraInfoService.updateCover(req,authUser));
    }


    @PostMapping(value = "/findCameraInfoAll")
    @ApiOperation(value = "区域分布图")
    public ResponseWrapper findCameraInfoAll(){
        return csipCameraInfoService.findCameraInfoAll();
    }



}
