package com.fsmer.csip.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fsmer.csip.entity.AlarmCount;
import com.fsmer.csip.entity.AuthUser;
import com.fsmer.csip.entity.CsipCameraInfo;
import com.fsmer.csip.entity.ai.RtspInfo;
import com.fsmer.csip.entity.ai.RtspRoot;
import com.fsmer.csip.entity.request.CameraInfoImagesReq;
import com.fsmer.csip.entity.request.CsipCameraInfoReq;
import com.fsmer.csip.entity.response.ResponseWrapper;
import com.fsmer.csip.enumeration.AICheckStatusEnum;
import com.fsmer.csip.enumeration.ResponseCode;
import com.fsmer.csip.repository.CsipAlarmHisRepository;
import com.fsmer.csip.repository.CsipCameraInfoRepository;
import com.fsmer.csip.service.ICsipCameraInfoService;
import com.fsmer.csip.util.async.AsyncService;
import com.fsmer.csip.util.http.HttpUtil;
import com.fsmer.csip.util.upload.Base64Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
public class CsipCameraInfoServiceImpl implements ICsipCameraInfoService {
    private static Logger logger = LoggerFactory.getLogger(CsipCameraInfoServiceImpl.class);

    @Autowired
    private CsipCameraInfoRepository csipCameraInfoRepository;

    @Autowired
    private CsipAlarmHisRepository alarmHisRepository;

    @Autowired
    private AsyncService asyncService;

    public ResponseWrapper findCameraInfoPage(CsipCameraInfoReq cameraInfo) {
        Pageable pageable=PageRequest.of(cameraInfo.getPageNum(), cameraInfo.getPageSize());
        Page<CsipCameraInfo> eList = csipCameraInfoRepository.findAll(new Specification<CsipCameraInfo>() {
            @Override
            public Predicate toPredicate(Root<CsipCameraInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (cameraInfo != null && !cameraInfo.getCameraId().equals(""))
                    predicates.add(cb.like(root.get("cameraId").as(String.class), "%" + cameraInfo.getCameraId() + "%"));
               if (cameraInfo!=null &&cameraInfo.getCameraType()!=null)
                    predicates.add(cb.equal(root.get("cameraType").as(String.class),cameraInfo.getCameraType() ));
                Predicate[] pre = new Predicate[predicates.size()];
                criteriaQuery.orderBy(cb.desc(root.get("createdTime").as(Timestamp.class)));
                criteriaQuery.where(predicates.toArray(pre));
                return cb.and(predicates.toArray(pre));
            }
        }, pageable);
        //List<RtspInfo> list = asyncService.getRtspInfos();
        Future<String> future = asyncService.getRtspInfos();
        String rtspInfoJson = null;
        int asyncState=0;
        List<RtspInfo> list=null;
        try {
            rtspInfoJson = future.get(800, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
            asyncState=1;
        }
        if (asyncState==0){
            RtspRoot robotResult = JSON.parseObject(rtspInfoJson).toJavaObject(RtspRoot.class);

            if (robotResult!=null){
                list= robotResult.getData().getRtsp_info();
            }
        }
        List<String> cameraIdsList = eList.stream().map(CsipCameraInfo::getCameraId).collect(Collectors.toList());
        List<AlarmCount> eventTypesg = alarmHisRepository.countAlarm(cameraIdsList, 0);
        List<AlarmCount> eventTyperq = alarmHisRepository.countAlarm(cameraIdsList, 1);
        Integer sgNum;
        Integer rqNum;
        for (CsipCameraInfo item :eList){
            eventTypesg.stream().filter(alarmCount -> alarmCount.getCameraId().equals(item.getCameraId())).collect(Collectors.toList()).forEach(AlarmCount->{
                item.setCountEventsg(AlarmCount.getAlarmNum().intValue());
            });
            eventTyperq.stream().filter(alarmCount -> alarmCount.getCameraId().equals(item.getCameraId())).collect(Collectors.toList()).forEach(AlarmCount->{
                item.setCountEventrq(AlarmCount.getAlarmNum().intValue());
            });
            sgNum=item.getCountEventsg()!=null?item.getCountEventsg():0;
            rqNum=item.getCountEventrq()!=null?item.getCountEventrq():0;
             item.setNotHandleNum(sgNum+rqNum);
            if (!CollectionUtils.isEmpty(list)){
            for (RtspInfo  sp:list){
                if(sp.getCamera_id().equals(item.getCameraId())){
                    item.setCheckStatus(AICheckStatusEnum.getName(sp.getStatus()));
                 break;
                }
            }
            }
        }
        return ResponseWrapper.createBySuccess(eList);
    }


    @Transactional
    @Override
    public ResponseWrapper addCameraInfo(CsipCameraInfo cameraInfo, AuthUser authUser) {
        cameraInfo.setCreatedBy(authUser.getUserId());
        cameraInfo.setCreatedByName(authUser.getUserName());
        cameraInfo.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        cameraInfo.setIsOnline("1");
        csipCameraInfoRepository.save(cameraInfo);
        return ResponseWrapper.createBySuccess();
    }

    @Transactional
    @Override
    public ResponseWrapper delCamerInfo(Integer id) {
        csipCameraInfoRepository.deleteById(id);
        return ResponseWrapper.createBySuccess();
    }

    @Override
    public ResponseWrapper findCameraInfoById(Integer id) {
        CsipCameraInfo cameraInfo = csipCameraInfoRepository.findById(id).orElse(null);
        Future<String> future= asyncService.getRtspInfos();
        try {
            String rtspInfoJson = future.get(800, TimeUnit.MILLISECONDS);
            RtspRoot robotResult = JSON.parseObject(rtspInfoJson).toJavaObject(RtspRoot.class);
            List<RtspInfo> list = robotResult.getData().getRtsp_info();
            if (!CollectionUtils.isEmpty(list)){
                for (RtspInfo  sp:list){
                    if(sp.getCamera_id().equals(cameraInfo.getCameraId())){
                        cameraInfo.setCheckStatus(AICheckStatusEnum.getName(sp.getStatus()));
                        break;
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
            return ResponseWrapper.createBySuccess(cameraInfo);
        }

        return ResponseWrapper.createBySuccess(cameraInfo);
    }

    @Override
    public ResponseWrapper updateCover(CameraInfoImagesReq req, AuthUser authUser){
        CsipCameraInfo po= csipCameraInfoRepository.getOne(req.getId());
        if (po == null)
            return  ResponseWrapper.createByErrorMessage(ResponseCode.OBJ_ON_EXISTENT.getMsg());
        if(req.getBase64().length()<8) return ResponseWrapper.createByErrorMessage(ResponseCode.ERROR.getMsg());
        String url= Base64Utils.generateImage(req.getBase64());
        po.setCoverUrl(url);
      return ResponseWrapper.createBySuccess(csipCameraInfoRepository.save(po));
    }

    @Override
    public ResponseWrapper findCameraInfoAll() {
        List<CsipCameraInfo> cameraInfos = csipCameraInfoRepository.findAll();
        JSONArray jsonArray = new JSONArray();
        for (CsipCameraInfo item : cameraInfos){
            JSONObject o = (JSONObject) JSON.toJSON(item);
            Integer countEventsg = alarmHisRepository.countEventByCameraId(0,String.valueOf(item.getId()));
            Integer countEventrq = alarmHisRepository.countEventByCameraId(1,String.valueOf(item.getId()));
            Integer notHandleAlarmNum = alarmHisRepository.countNotHandleAlarmInfoByCameraId(String.valueOf(item.getId()));
            JSONObject other = new JSONObject();
            other.put("countEventsg", countEventsg);
            other.put("countEventrq",countEventrq);
            other.put("notHandleAlarmNum",notHandleAlarmNum);
            o.put("other",other);

            jsonArray.add(o);
        }
        return ResponseWrapper.createBySuccess(jsonArray);
    }

}
