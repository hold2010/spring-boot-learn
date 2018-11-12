package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="zcm.gateway")
// 好像不配置也行
@PropertySource(value = {"classpath:config/gatewayConfig.properties"}, encoding = "UTF-8", ignoreResourceNotFound = true)
public class GateWayProperties {

    private String apiServer;

    private String nginxHome;

    private String haproxyHome;

    public String getApiServer() {
        return apiServer;
    }

    public void setApiServer(String apiServer) {
        this.apiServer = apiServer;
    }

    public String getNginxHome() {
        return nginxHome;
    }

    public void setNginxHome(String nginxHome) {
        this.nginxHome = nginxHome;
    }

    public String getHaproxyHome() {
        return haproxyHome;
    }

    public void setHaproxyHome(String haproxyHome) {
        this.haproxyHome = haproxyHome;
    }
}
