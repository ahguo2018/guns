package com.stylefeng.guns.rest.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @date 2019/7/4 20:29
 */
@Data
@Configuration
@ConfigurationProperties(prefix = FtpProperties.FTP_PREFIX)
public class FtpProperties {

    public static final String FTP_PREFIX = "ftp";
    /* ip */
    private String hostName;
    /* 端口 */
    private Integer port;
    /* 用户名 */
    private String username;
    /* 密码 */
    private String password;
    /* 上传路径 */
    private String uploadPath;

}
