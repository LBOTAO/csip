package com.fsmer.csip.repository;

import com.fsmer.csip.entity.CsipCameraInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CsipCameraInfoRepository extends JpaRepository<CsipCameraInfo,Integer>, JpaSpecificationExecutor<CsipCameraInfo> {
}
