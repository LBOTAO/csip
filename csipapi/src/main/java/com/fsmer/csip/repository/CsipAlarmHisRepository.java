package com.fsmer.csip.repository;

import com.fsmer.csip.entity.AlarmCount;
import com.fsmer.csip.entity.CsipAlarm;
import com.fsmer.csip.entity.CsipAlarmHis;
import com.fsmer.csip.entity.request.CsipAlarmReq;
import com.fsmer.csip.entity.response.ResponseWrapper;
import com.fsmer.csip.entity.vo.EchartsPieChartVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

/**
 * @program: csipapi
 * @description:
 * @author: Tracy
 * @create: 2021-01-25 16:31
 */
public interface CsipAlarmHisRepository extends JpaRepository<CsipAlarmHis, String>, JpaSpecificationExecutor<CsipAlarmHis> {
    CsipAlarmHis findByAlarmId(Integer alarmId);

    @Query(value = "select DISTINCT function('date_format',h.alarmTime,'%Y-%m-%d') from CsipAlarmHis h where function('date_format', h.alarmTime,'%Y-%m')=:alarmTime")
    List<String> findAlarmByAlarmTime(@Param("alarmTime")String alarmTime);

    @Query(value = "select count(h.eventType) from CsipAlarmHis h where h.eventType=?1")
    Integer countEventType(Integer eventType);

    @Query(value = "select count(h.eventType) from CsipAlarmHis h where h.eventType=?1 and h.cameraId=?2")
    Integer countEventByCameraId(Integer eventType,String cameraId);

    @Query(value = "select count(h.alarmHisId) from CsipAlarmHis h where h.cameraId=?1")
    Integer countNotHandleAlarmInfoByCameraId(String cameraId);

    @Modifying
    @Query(nativeQuery = true, value = "update csip_alarm_his set alarm_status=:alarmStatus,alarm_start_time=:alarmStartTime where alarm_id=:alarmId ")
    int updateAlarmHisStartTimeStatus(Integer alarmStatus, Integer alarmId, Timestamp alarmStartTime);

    @Modifying
    @Query(nativeQuery = true, value = "update csip_alarm_his set alarm_status=:alarmStatus,alarm_end_time=:alarmEndTime  where alarm_id=:alarmId ")
    int updateAlarmHisEndTimeStatus(Integer alarmStatus, Integer alarmId,Timestamp alarmEndTime);

    @Query("select new com.fsmer.csip.entity.AlarmCount(h.cameraId,count(h.cameraId)) from CsipAlarmHis h where h.cameraId in(:cameraIds) and h.eventType=:eventType group by h.cameraId")
    List<AlarmCount> countAlarm(List<String> cameraIds,Integer eventType);

}
