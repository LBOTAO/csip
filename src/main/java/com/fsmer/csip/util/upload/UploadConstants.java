package com.fsmer.csip.util.upload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.MultipartConfigElement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Component
public class UploadConstants {
    public static String BASE_PATH = "E:/javaCode/svn/csip/trunk/web/csip/public/images/";
    public static String HTTP_PATH = "http//:localhost:8080/csip/images/";
    @Value("${upload.basePath}")
    public void setUploadBasePath(String basePath) {
        UploadConstants.BASE_PATH = basePath;
    }
    @Value("${upload.httpPath}")
    public void setHttpPath(String httpPath) {
        UploadConstants.HTTP_PATH = httpPath;
    }

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation(UploadConstants.BASE_PATH);
        return factory.createMultipartConfig();
    }
    /**
     * @Description: 获取图片后缀
     * @Param: [file]
     * @return: java.lang.String
     * @date: 2019/8/24 15:27
     */
    public static String getSuffixName(MultipartFile file){
        String fileName = file.getOriginalFilename();
        return fileName.substring(fileName.lastIndexOf("."));
    }
    /**
     * @Description: 生成文件名称通用方法
     * @Param: [suffixName] 图片后缀
     * @return: java.lang.String
     * @date: 2019/8/24 15:29
     */
    public static String getNewFileName(String suffixName){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        int random = new Random().nextInt(100);
        StringBuilder tempName = new StringBuilder();
        tempName.append(sdf.format(new Date())).append(random).append(".").append(suffixName);
        return tempName.toString();
    }

    public static String getFileExtendName(byte[] photoByte) {
        String strFileExtendName = ".jpg";
        if ((photoByte[0] == 71) && (photoByte[1] == 73) && (photoByte[2] == 70)
                && (photoByte[3] == 56) && ((photoByte[4] == 55) || (photoByte[4] == 57))
                && (photoByte[5] == 97)) {
            strFileExtendName = ".gif";
        } else if ((photoByte[6] == 74) && (photoByte[7] == 70) && (photoByte[8] == 73)
                && (photoByte[9] == 70)) {
            strFileExtendName = ".jpg";
        } else if ((photoByte[0] == 66) && (photoByte[1] == 77)) {
            strFileExtendName = ".bmp";
        } else if ((photoByte[1] == 80) && (photoByte[2] == 78) && (photoByte[3] == 71)) {
            strFileExtendName = ".png";
        }
        return strFileExtendName;
    }
}
