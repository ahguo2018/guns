package com.stylefeng.guns.rest.common.util;

import com.stylefeng.guns.rest.config.properties.FtpProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * @date 2019/7/4 20:38
 * Ftp工具类
 */
@Slf4j
@Component
public class FtpUtil {

    private FTPClient ftpClient = null;
    @Autowired
    private FtpProperties ftpProperties;

    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 初始化ftp服务器
     */
    private void initFtpClient() {
        ftpClient = new FTPClient();
        ftpClient.setControlEncoding(DEFAULT_CHARSET);
        try {
            log.info("connecting ... ftp服务器:host={},port={}", ftpProperties.getHostName(), ftpProperties.getPort());
            ftpClient.connect(ftpProperties.getHostName(), ftpProperties.getPort());
            ftpClient.login(ftpProperties.getUsername(), ftpProperties.getPassword());
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                log.info("connect failed...ftp服务器:host={},port={}", ftpProperties.getHostName(), ftpProperties.getPort());
            }
            log.info("connect successful...ftp服务器:host={},port={}", ftpProperties.getHostName(), ftpProperties.getPort());
        } catch (IOException e) {
            log.error("初始化ftp失败", e);
        }
    }

    /**
     * 根据文件路径获取文件内容
     * @param filePath 文件路径
     * @return
     */
    public String getFileStrByPath(String filePath) {
        BufferedReader bufferedReader = null;
        try {
            initFtpClient();
            bufferedReader = new BufferedReader(new InputStreamReader(ftpClient.retrieveFileStream(filePath)));
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line);
            }
            ftpClient.logout();
            return buffer.toString();
        } catch (IOException e) {
            log.error("获取文件信息失败", e);
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != bufferedReader) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 上传文件
     * @param fileName 文件名称
     * @param file 待上传文件
     * @return
     */
    public boolean uploadFile(String fileName, File file){
        boolean flag = false;
        InputStream inputStream = null;
        try {
            log.info("开始上传文件...");
            inputStream = new FileInputStream(file);
            initFtpClient();
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            //切换工作目录
            ftpClient.changeWorkingDirectory(ftpProperties.getUploadPath());
            //上传文件
            ftpClient.storeFile(fileName,inputStream);
            ftpClient.logout();
            flag = true;
            log.info("上传文件成功!");
        } catch (Exception e) {
            log.error("上传文件失败",e);
        }finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }


    public static void main(String[] args) {
        FtpUtil ftpUtil = new FtpUtil();
        String fileStrByPath = ftpUtil.getFileStrByPath("/1.txt");
        System.out.println(fileStrByPath);
    }


}
