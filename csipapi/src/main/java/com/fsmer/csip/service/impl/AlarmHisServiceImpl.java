package com.fsmer.csip.service.impl;

import com.fsmer.csip.entity.AuthUser;
import com.fsmer.csip.entity.CsipAlarm;
import com.fsmer.csip.entity.CsipAlarmHis;
import com.fsmer.csip.entity.request.CsipAlarmReq;
import com.fsmer.csip.entity.response.ResponseWrapper;
import com.fsmer.csip.repository.CsipAlarmDetailRepository;
import com.fsmer.csip.repository.CsipAlarmHisRepository;
import com.fsmer.csip.repository.CsipAlarmRepository;
import com.fsmer.csip.service.IAlarmHisService;
import com.fsmer.csip.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class AlarmHisServiceImpl implements IAlarmHisService {
    @Autowired
    private CsipAlarmRepository csipAlarmRepository;
    @Autowired
    private CsipAlarmHisRepository csipAlarmHisRepository;
    @Autowired
    EntityManager entityManager;
    @Autowired
    private CsipAlarmDetailRepository csipAlarmDetailRepository;

    @Override
    public ResponseWrapper handleAlarmInfo(Integer alarmId, String handleOpinion, AuthUser authUser) {
        CsipAlarmHis csipAlarmHis = csipAlarmHisRepository.findByAlarmId(alarmId);
        CsipAlarm csipAlarm = csipAlarmRepository.findByAlarmId(alarmId);
        if (csipAlarmHis==null || csipAlarm==null) ResponseWrapper.createByErrorMessage("处理对象不存在");
        csipAlarmHis.setHandleOpinion(handleOpinion);
        csipAlarmHis.setHandleState("0");
        csipAlarmHis.setHandleTime(new Timestamp(System.currentTimeMillis()));
        csipAlarmHis.setHandleBy(authUser.getUserId());
        csipAlarmHis.setHandleByName(authUser.getUserName());
        csipAlarmHisRepository.save(csipAlarmHis); //修改历史告警信息处理状态
        csipAlarmRepository.delete(csipAlarm);  //删除告警表未处理数据
        return ResponseWrapper.createBySuccess();
    }



    @Override
    public CsipAlarmHis findByAlarmId(Integer alarmId) {
        return csipAlarmHisRepository.findByAlarmId(alarmId);
    }

    @Override
    public ResponseWrapper findAlarmHisPage(CsipAlarmReq alarmReq) {
        Pageable pageable= PageRequest.of(alarmReq.getPageNum(), alarmReq.getPageSize());
        Page<CsipAlarmHis> eList = csipAlarmHisRepository.findAll(new Specification<CsipAlarmHis>() {
            @Override
            public Predicate toPredicate(Root<CsipAlarmHis> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Path<Integer> alarmLocation = root.get("alarmLocation"); //告警地址
                Path<Integer> handleState = root.get("handleState"); //处理状态：0：已处理 :1：未处理
                Path<Integer> eventType = root.get("eventType");  //事件类型 ：  0：施工作业 1：人员入侵
                Path<Integer> alarmTime=root.get("alarmTime");   //告警事件
                List<Predicate> predicates = new ArrayList<>();

                if (alarmReq.getAlarmLocation() != null && !alarmReq.getAlarmLocation().equals("")) {
                    predicates.add(cb.like(alarmLocation.as(String.class), "%" + alarmReq.getAlarmLocation() + "%"));
                }
                if (alarmReq.getHandleState() != null && !alarmReq.getHandleState().equals("") && alarmReq.getHandleState()!=2) {
                    predicates.add(cb.equal(handleState.as(Integer.class), alarmReq.getHandleState()));
                }
                if (alarmReq.getEventType() != null && alarmReq.getEventType()!=null && alarmReq.getEventType()!=2) {  //查询指定事件类型
                    predicates.add(cb.equal(eventType.as(Integer.class), alarmReq.getEventType()));
                }
                //起始日期
                if (alarmReq.getStartTime() != null && !alarmReq.getStartTime().trim().equals("")) {
                    predicates.add(cb.greaterThanOrEqualTo(alarmTime.as(String.class), DateTimeUtil.dealDateFormat(alarmReq.getStartTime())));
                }
                //结束日期
                if (alarmReq.getEndTime() != null && !alarmReq.getEndTime().trim().equals("")) {
                    predicates.add(cb.lessThanOrEqualTo(alarmTime.as(String.class), DateTimeUtil.dealDateFormat(alarmReq.getEndTime())));
                }
                predicates.add(cb.notEqual(handleState.as(Integer.class),1));
                Predicate[] pre = new Predicate[predicates.size()];

                criteriaQuery.orderBy(cb.desc(root.get("alarmTime").as(Timestamp.class)));
                criteriaQuery.where(predicates.toArray(pre));
                return cb.and(predicates.toArray(pre));
            }
        }, pageable);
        return ResponseWrapper.createBySuccess(eList);
    }

    @Override
    public ResponseWrapper getAlarmInfoHisByAlarmHisId(Integer alarmHisId) {
        return null;
    }
    public   ResponseWrapper getAlarmIdByDetail(Integer alarmId){
        return ResponseWrapper.createBySuccess(csipAlarmDetailRepository.findByAlarmId(alarmId));
    }
    @Override
    public ResponseWrapper findAlarmByAlarmTime(String alarmTime) {
        String alarmTimeStr = DateTimeUtil.YYYY_MM_DD_HH_MM_SS(alarmTime);
        return ResponseWrapper.createBySuccess(csipAlarmHisRepository.findAlarmByAlarmTime(alarmTimeStr.substring(0, 7)));
    }

    @Override
    public Integer countEventType(Integer eventType) {
        return csipAlarmHisRepository.countEventType(eventType);
    }

    @Override
    public Integer getEventCountByDate(String markNum, Integer eventType) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT COUNT(event_type) FROM csip_alarm_his ");
        switch (markNum){
            case "0":
                sql.append(" WHERE TO_DAYS(created_time)=TO_DAYS(NOW()) ");//日统计
                break;
            case "1":
                sql.append(" WHERE TO_DAYS(CURDATE()) - TO_DAYS(created_time)<=7 ");  //周统计
                break;
            case "2":
                sql.append(" WHERE DATE_FORMAT( created_time, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' ) ");  //月统计
                break;
            case "3":
                sql.append(" WHERE YEAR(created_time)=YEAR(NOW()) ");  //年统计
                break;
        }
        sql.append(" and event_type= ? ");
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter(1,eventType);
        Object obj = query.getSingleResult();
        Integer resultCountNum = Integer.valueOf(obj.toString());
        return resultCountNum;
    }
}
