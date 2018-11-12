package com.example.demo.test;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class TestDing {

    // webhook
    public static String WEBHOOK_TOKEN = "https://oapi.dingtalk.com/robot/send?access_token=14c1027b168a95a6d59c50edbc0fc04ad6094c7c1b0bd872a06cbd697b81bab3";

    public static void main(String[] args) throws Exception{

        HttpClient httpclient = HttpClients.createDefault();

        HttpPost httppost = new HttpPost(WEBHOOK_TOKEN);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");

        String textMsg = "{ \"msgtype\": \"text\", \"text\": {\"content\": \"早上好 O(∩_∩)O\"}," +
                " \"at\": {\"isAtAll\": true }}";
        System.out.println(textMsg);
        StringEntity se = new StringEntity(textMsg, "utf-8");
        httppost.setEntity(se);

        HttpResponse response = httpclient.execute(httppost);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            String result = EntityUtils.toString(response.getEntity(), "utf-8");
            System.out.println(result);
        }
    }
}
