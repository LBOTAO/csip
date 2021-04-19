package com.fsmer.csip.util.async;

import com.alibaba.fastjson.JSON;
import com.fsmer.csip.entity.ai.RtspInfo;
import com.fsmer.csip.entity.ai.RtspRoot;
import com.fsmer.csip.util.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Future;

@Component
public class AsyncService {


    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    @Value("${aivideo.baseUrl}")
    private String aiVideoBaseUrl;

    @Async("taskExecutor")
    public Future<String> getRtspInfos() {
        try{
            //获取AI视频分析服务器
            String json= HttpUtil.doGet(aiVideoBaseUrl.concat("aivideo/v1.0/getRtspList"),null);
            RtspRoot robotResult = JSON.parseObject(json).toJavaObject(RtspRoot.class);
            if (robotResult.getCode()!=0)
                logger.info("--->>AI接口条用失败");
            return new AsyncResult<String>(json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
