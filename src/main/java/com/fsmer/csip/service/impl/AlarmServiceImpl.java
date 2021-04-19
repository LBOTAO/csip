package com.fsmer.csip.service.impl;

import com.alibaba.fastjson.JSON;
import com.fsmer.csip.entity.CsipAlarm;
import com.fsmer.csip.entity.CsipAlarmDetail;
import com.fsmer.csip.entity.CsipAlarmHis;
import com.fsmer.csip.entity.request.CsipAlarmReq;
import com.fsmer.csip.entity.response.ResponseWrapper;
import com.fsmer.csip.entity.vo.AlarmVO;
import com.fsmer.csip.repository.CsipAlarmDetailRepository;
import com.fsmer.csip.repository.CsipAlarmHisRepository;
import com.fsmer.csip.repository.CsipAlarmRepository;
import com.fsmer.csip.service.IAlarmService;
import com.fsmer.csip.util.DateTimeUtil;
import com.fsmer.csip.ws.AlarmWebSocket;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: csipapi
 * @description:
 * @author: Tracy
 * @create: 2021-01-25 16:10
 */
@Service
public class AlarmServiceImpl  implements IAlarmService {
    @Autowired
    private CsipAlarmRepository csipAlarmRepository;
    @Autowired
    private CsipAlarmHisRepository csipAlarmHisRepository;
    @Autowired
    private CsipAlarmDetailRepository csipAlarmDetailRepository;

    @Override
    @Transactional
    public ResponseWrapper saveAlarm(AlarmVO vo) {
        CsipAlarm po=null;
        try {
        List<CsipAlarm>  list=csipAlarmRepository.findByEventId(vo.getEvent_id());//查询是否产生消息
         if (!CollectionUtils.isEmpty(list)){
                 po= list.get(0);
            if (po==null)
                return  ResponseWrapper.createByErrorMessage("对象不存在");
            if (vo.getAlarm_type().intValue()==1) {
                csipAlarmRepository.updateAlarmStartTimeStatus(vo.getAlarm_type(), po.getAlarmId(), DateTimeUtil.longToTimestamp(vo.getAlarm_time()));//更新状态*/
                csipAlarmHisRepository.updateAlarmHisStartTimeStatus(vo.getAlarm_type(), po.getAlarmId(), DateTimeUtil.longToTimestamp(vo.getAlarm_time()));//更新状态*/
            }else if (vo.getAlarm_type().intValue()==3) {
                csipAlarmRepository.updateAlarmEndTimeStatus(vo.getAlarm_type(), po.getAlarmId(), DateTimeUtil.longToTimestamp(vo.getAlarm_time()));//更新状态*/
                csipAlarmHisRepository.updateAlarmHisEndTimeStatus(vo.getAlarm_type(), po.getAlarmId(), DateTimeUtil.longToTimestamp(vo.getAlarm_time()));//更新状态*/
            }
         }else{
            CsipAlarm csipAlarm = new CsipAlarm(vo);
             po = csipAlarmRepository.save(csipAlarm);
            if (po.getAlarmId() > 0) {
                CsipAlarmHis his = new CsipAlarmHis();
                BeanUtils.copyProperties(po,his);
                csipAlarmHisRepository.save(his);
            }
          }
            CsipAlarmDetail entity= new CsipAlarmDetail(po,vo);
            csipAlarmDetailRepository.save(entity);
            AlarmWebSocket.sendMesage(JSON.toJSONString(1));
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseWrapper.createBySuccess();
    }

    @Override
    public ResponseWrapper findAlarmPage(CsipAlarmReq alarmReq) {
        Pageable pageable= PageRequest.of(alarmReq.getPageNum(), alarmReq.getPageSize());
        Page<CsipAlarm> eList = csipAlarmRepository.findAll(new Specification<CsipAlarm>() {
            @Override
            public Predicate toPredicate(Root<CsipAlarm> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Path<Integer> alarmLocation = root.get("alarmLocation"); //告警地址
                Path<Integer> eventType = root.get("eventType");  //事件类型 ：施工作业  人员入侵
                Path<Integer> alarmTime=root.get("alarmTime");
                List<Predicate> predicates = new ArrayList<>();
                if (alarmReq.getAlarmLocation() != null && !alarmReq.getAlarmLocation().equals("")) {
                    predicates.add(cb.like(alarmLocation.as(String.class), "%" + alarmReq.getAlarmLocation() + "%"));
                }
                if (alarmReq.getEventType() != null && alarmReq.getEventType()!=null && alarmReq.getEventType()!=2) {  //查询指定事件类型
                    predicates.add(cb.equal(eventType.as(Integer.class), alarmReq.getEventType()));
                }
                 //起始日期
                if (alarmReq.getStartTime() != null && !alarmReq.getStartTime().trim().equals("")) {
                    predicates.add(cb.greaterThanOrEqualTo(alarmTime.as(String.class),DateTimeUtil.dealDateFormat(alarmReq.getStartTime())));
                }
                //结束日期
                if (alarmReq.getEndTime() != null && !alarmReq.getEndTime().trim().equals("")) {
                    predicates.add(cb.lessThanOrEqualTo(alarmTime.as(String.class), DateTimeUtil.dealDateFormat(alarmReq.getEndTime())));
                }
                Predicate[] pre = new Predicate[predicates.size()];
                criteriaQuery.orderBy(cb.desc(root.get("alarmTime").as(Timestamp.class)));
                criteriaQuery.where(predicates.toArray(pre));

                return cb.and(predicates.toArray(pre));
            }
        }, pageable);
        return ResponseWrapper.createBySuccess(eList);
    }

    @Override
    public ResponseWrapper findAlarmInfoByNotHandle() {
        return ResponseWrapper.createBySuccess(csipAlarmRepository.findAll());
    }

    @Override
    @Transactional
    public ResponseWrapper saveOffLineData(MultipartFile file,String fileUrl) {
        CsipAlarm po=null;
        String originalFilename = file.getOriginalFilename();
        String fileName=originalFilename.substring(0, originalFilename.indexOf("."));
       Integer count= csipAlarmDetailRepository.isExistImgName(originalFilename);
       if (count>0)
           return ResponseWrapper.createByErrorMessage("有数据重复,重复数据将上传失败");
        String[] fileArr = fileName.split("_");
        if (fileArr.length!=6) return ResponseWrapper.createByErrorMessage("图片格式不正确");
        Timestamp alarmTime = DateTimeUtil.longToTimestamp(Long.valueOf(fileArr[2]));
        List<CsipAlarm>  list=csipAlarmRepository.findByEventId(fileArr[3]);//判断此条告警信息是否已存在
        if (!CollectionUtils.isEmpty(list)){
            po = list.get(0);
            if (fileArr[4]=="1"){   //开始告警 状态
                csipAlarmRepository.updateAlarmStartTimeStatus(Integer.valueOf(fileArr[4]), po.getAlarmId(),alarmTime);//更新状态*/
                csipAlarmHisRepository.updateAlarmHisStartTimeStatus(Integer.valueOf(fileArr[4]), po.getAlarmId(), alarmTime);//更新状态*/
            }else if (fileArr[4]=="2"){   //结束告警 状态
                csipAlarmRepository.updateAlarmEndTimeStatus(Integer.valueOf(fileArr[4]), po.getAlarmId(), alarmTime);//更新状态 */
                csipAlarmHisRepository.updateAlarmHisEndTimeStatus(Integer.valueOf(fileArr[4]), po.getAlarmId(), alarmTime);//更新状态*/
            }

        }else{
            //1000236_    dd机房   _1617876726861_1617876726861_1_0
            //camera_id+ location+ timestamp+ event_id+ alarm_type+ event_type
            po=new CsipAlarm(fileArr[3],fileArr[0],fileArr[1],Integer.valueOf(fileArr[5]),alarmTime,Integer.valueOf(fileArr[4]));
            CsipAlarm ca = csipAlarmRepository.save(po);
            CsipAlarmHis his = new CsipAlarmHis();
            BeanUtils.copyProperties(ca,his);
            csipAlarmHisRepository.save(his);
        }
        csipAlarmDetailRepository.save(new CsipAlarmDetail(po.getAlarmId(),fileUrl,originalFilename));
        AlarmWebSocket.sendMesage(JSON.toJSONString(1));
        return ResponseWrapper.createBySuccess();
    }

    @Override
    public int findAlarmInfoCount() {
        int num=(int) csipAlarmRepository.count();
        AlarmWebSocket.sendMesage(JSON.toJSONString(num));
        return num;
    }
}
