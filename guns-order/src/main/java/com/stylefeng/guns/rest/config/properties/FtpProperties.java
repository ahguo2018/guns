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

    private String hostName;
    private Integer port;
    private String username;
    private String password;

}
