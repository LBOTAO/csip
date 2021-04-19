package com.fsmer.csip;

import com.fsmer.csip.util.LogUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LogTest {

    @Test
    public void logTest() {
        Logger log = LogUtil.getExceptionLogger();
        Logger log1 = LogUtil.getBussinessLogger();
        Logger log2 = LogUtil.getDBLogger();

        log.error("getExceptionLogger===日志测试");
        log1.info("getBussinessLogger===日志测试");
        log2.debug("getDBLogger===日志测试");

    }
}
