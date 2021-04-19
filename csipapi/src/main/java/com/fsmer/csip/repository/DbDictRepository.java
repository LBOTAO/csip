package com.fsmer.csip.repository;

import com.fsmer.csip.entity.DbDict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DbDictRepository extends JpaRepository<DbDict, String>, JpaSpecificationExecutor<DbDict> {

    List<DbDict> findByDictKind(String dictKind);
}
