package com.stylefeng.guns.rest.common.util;

import com.stylefeng.guns.rest.config.properties.FtpProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @date 2019/7/4 20:38
 */
@Slf4j
@Component
public class FtpUtil {

    private FTPClient ftpClient = null;
    @Autowired
    private FtpProperties ftpProperties;

    private void initFtpClient(){
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(ftpProperties.getHostName(),ftpProperties.getPort());
            ftpClient.login(ftpProperties.getUsername(),ftpProperties.getPassword());
        } catch (IOException e) {
            log.error("初始化ftp失败",e);
        }
    }

    //输入一个路径,返回路径文件中的字符串
    public String getFileStrByPath(String filePath){
        BufferedReader bufferedReader = null;
        try {
            initFtpClient();
            bufferedReader = new BufferedReader(new InputStreamReader(ftpClient.retrieveFileStream(filePath)));
            StringBuffer buffer = new StringBuffer();
            String line;
            while((line=bufferedReader.readLine())!=null){
              buffer.append(line);
            }
            ftpClient.logout();
            return buffer.toString();

        } catch (IOException e) {
            log.error("获取文件信息失败",e);
        }finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static void main(String[] args) {
        FtpUtil ftpUtil = new FtpUtil();
        String fileStrByPath = ftpUtil.getFileStrByPath("/1.txt");
        System.out.println(fileStrByPath);
    }



}
