package com.example.demo.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.json.XML;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Json2Xml {

    public String readJson(String inputFile) {
        // read input file
        String content = "";
        try {
            File file = new File(inputFile);
            content = FileUtils.readFileToString(file, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * json to xml
     * @param json
     * @return
     */
    public static String json2xml(String json) {
        JSONObject jsonObj = new JSONObject(json);
        return "<xml>" + XML.toString(jsonObj) + "</xml>";
    }

    /**
     * xml to json
     * @param xml
     * @return
     */
    public static String xml2json(String xml) {
        JSONObject xmlJSONObj = XML.toJSONObject(xml.replace("<xml>", "").replace("</xml>", ""));
        return xmlJSONObj.toString();
    }

    public static void main(String[] args) {

        String inputFile = "D:\\ZCM\\myself\\demo\\data\\app.json";

        Json2Xml my = new Json2Xml();
        String content = my.readJson(inputFile);

        //json串
        String jsonStr = JSON.toJSONString(content);
        System.out.println("source json : " + jsonStr);

        //json转xml
        String xml = json2xml(jsonStr);
        System.out.println("xml  :  " + xml);
//        //xml转json
//        String targetJson = xml2json(xml);
//        System.out.println("target json : " + targetJson);
    }


}
