package com.fsmer.csip.repository;

import com.fsmer.csip.entity.CsipAlarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface CsipAlarmRepository extends JpaRepository<CsipAlarm, String>, JpaSpecificationExecutor<CsipAlarm> {


    List<CsipAlarm> findByEventId(String  eventId);
    @Modifying
    @Query(nativeQuery = true, value = "update csip_alarm set alarm_status=:alarmStatus,alarm_start_time=:alarmStartTime where alarm_id=:alarmId ")
    int updateAlarmStartTimeStatus(Integer alarmStatus, Integer alarmId, Timestamp alarmStartTime);

    @Modifying
    @Query(nativeQuery = true, value = "update csip_alarm set alarm_status=:alarmStatus,alarm_end_time=:alarmEndTime  where alarm_id=:alarmId ")
    int updateAlarmEndTimeStatus(Integer alarmStatus, Integer alarmId,Timestamp alarmEndTime);

    CsipAlarm findByAlarmId(Integer alarmId);
}
