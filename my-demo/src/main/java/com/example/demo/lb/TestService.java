package com.example.demo.lb;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class TestService {

    public void reload(String data) {

        // 将配置保存到本地
        File dest = new File("./data/gateway.json");
        try {
            FileUtils.writeStringToFile(dest, data);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        // 发出通知重载配置
    }
}
