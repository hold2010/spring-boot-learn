package com.example.demo.util;

import org.apache.commons.io.FileUtils;

import java.io.File;

public class FileUtil {

    public static int div(int i,int j) throws Exception{    // 定义除法操作，如果有异常，则交给被调用处处理
        int temp = i / j ;    // 计算，但是此处有可能出现异常
        return temp ;
    }

    public static void main(String[] args) {

        File file = new File("D:\\mycode\\java\\my-demo\\data\\nginx.conf");
        File dest = new File("D:\\mycode\\java\\my-demo\\src\\main\\resources\\config\\nginx1.conf");
        File test = new File("./conf/te.conf");
        System.out.println(test.getAbsolutePath()) ;

        try {
            FileUtils.copyFile(file, test);
            System.out.println("除法操作：" + div(10,2)) ;
            // 配置刷新
//            logger.info("Reload nginx...");
//            ShellUtil.execute("nginx -s reload");
//            logger.info("Reload successfully:{}", result);
        } catch (Exception e) {
//            logger.warn("Failed to reload nginx,error:");
//            logger.warn(e);
//            result = e.toString();
            e.printStackTrace();
        }


    }


}
