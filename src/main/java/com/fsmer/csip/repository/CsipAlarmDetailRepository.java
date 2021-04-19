package com.fsmer.csip.repository;

import com.fsmer.csip.entity.CsipAlarmDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CsipAlarmDetailRepository extends JpaRepository<CsipAlarmDetail, String>, JpaSpecificationExecutor<CsipAlarmDetail> {



    List<CsipAlarmDetail> findByAlarmId(Integer alarmId);

    @Query("SELECT count(1)  from CsipAlarmDetail where img_name=:imgName")
    Integer isExistImgName(String imgName);
}
