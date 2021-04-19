package com.fsmer.csip.util.upload;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * @program: csipapi
 * @description:
 * @author: Tracy
 * @create: 2021-02-05 09:53
 */
public class Base64Utils {

        /**
         * @Description: 将base64编码字符串转换为图片
         * @Author: tracy
         * @return
         */
        public static String generateImage(String  base64Code) {
            // 解密
            try {
                String[] file =base64Code.split("base64,");
                String extendName=file[0].split("/")[1].replace(";","");
                byte[] b = Base64.decodeBase64(file[1]);
                String fileName=  UploadConstants.getNewFileName(extendName);
                String savePath = UploadConstants.BASE_PATH.concat(fileName);
                String imgClassPath = UploadConstants.HTTP_PATH.concat(fileName);
                // 处理数据
                for (int i = 0; i < b.length; ++i) {
                    if (b[i] < 0)  b[i] += 256;
                }
                File outFile = new File(savePath);
                OutputStream out = new FileOutputStream(outFile);
                out.write(b);
                out.flush();
                out.close();
                return imgClassPath;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

    /**
     * 上传文件
     * @param file  接收的文件
     * @param categoryName  类别文件夹名称
     * @throws IOException
     */
    public static String uploadFile(MultipartFile file,String categoryName) {
        //创建年月日文件夹
        Calendar date = Calendar.getInstance();
        File dateDirs = new File(date.get(Calendar.YEAR)
                + File.separator + (date.get(Calendar.MONTH) + 1) + File.separator + (date.get(Calendar.DATE)));
        //新文件
        String fileUrl=categoryName+ File.separator + dateDirs + File.separator + file.getOriginalFilename();
        File newFile = new File(UploadConstants.BASE_PATH +File.separator+fileUrl);
        //判断目标文件所在的目录是否存在
        if (!newFile.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            newFile.getParentFile().mkdirs();
        }
        //将内存中的数据写入磁盘
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String urlFile = fileUrl.replace("\\", "/");
        String returnFileUrl=UploadConstants.HTTP_PATH+urlFile;
        return returnFileUrl;
    }



    public static     List<String> uploadFile(MultipartFile fileList[],String categoryName) {
        List<String> pathList=new ArrayList<>();
        Set<String> fileNameList=new HashSet<>();
        List<MultipartFile> resultList=new ArrayList<>();
        //创建年月日文件夹
        Calendar date = Calendar.getInstance();
        for (MultipartFile file:fileList){
            fileNameList.add(file.getName());
            resultList.add(file);
        }
        for (MultipartFile file:resultList){
            File dateDirs = new File(date.get(Calendar.YEAR)+ File.separator + (date.get(Calendar.MONTH) + 1) + File.separator + (date.get(Calendar.DATE)));
            //新文件
            String fileUrl=categoryName+ File.separator + dateDirs + File.separator + file.getOriginalFilename();
            File newFile = new File(UploadConstants.BASE_PATH +File.separator+fileUrl);
            //判断目标文件所在的目录是否存在
            if (!newFile.getParentFile().exists()) {
                //如果目标文件所在的目录不存在，则创建父目录
                newFile.getParentFile().mkdirs();
            }
            //将内存中的数据写入磁盘
            try {
                file.transferTo(newFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String urlFile = fileUrl.replace("\\", "/");
            String returnFileUrl=UploadConstants.HTTP_PATH+urlFile;
            pathList.add(UploadConstants.HTTP_PATH+urlFile);
        }

        return pathList;
    }
}


