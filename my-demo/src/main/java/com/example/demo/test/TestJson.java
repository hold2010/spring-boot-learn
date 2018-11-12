package com.example.demo.test;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class TestJson {

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

    public List parseArray(JSONArray jsonArray) {
        List<HashMap<String, String>> mapList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            Object value = jsonArray.get(i);
            if (value instanceof JSONObject) {
                HashMap<String, String> data = new HashMap<>();
                JSONObject jsonObject = (JSONObject)value;
                for(String key : jsonObject.keySet()) {
                    data.put(key, jsonObject.getString(key));
                }
                mapList.add(data);
            }
        }
        return mapList;
    }

    public HSSFWorkbook getExcelData(String content) {
        // create excel
        HSSFWorkbook workbook = new HSSFWorkbook();
        JSONObject appInfo = JSONObject.parseObject(content).getJSONObject("appInfo");
        // 做结构变更
        JSONArray configFileMapping = appInfo.getJSONObject("configFileManageInfo").getJSONArray("configFileMapping");
        appInfo.put("configFileMapping", configFileMapping);

        for (String key : appInfo.keySet()) {
            Object value = appInfo.get(key);
            if ("status".equals(key) || "configFileManageInfo".equals(key)) {
                continue;
            }
            // create sheet for every key except status
            HSSFSheet sheet = workbook.createSheet(key);
            HSSFRow headRow = sheet.createRow(0);
            if ("endpoints".equals(key)) {
                headRow.createCell(0).setCellValue(key);
                JSONArray endpoints = (JSONArray) value;
                for (int i = 0; i < endpoints.size(); i++) {
                    HSSFRow bodyRow = sheet.createRow(i+1);
                    bodyRow.createCell(0).setCellValue(endpoints.getString(i));
                }
                sheet.autoSizeColumn(0);
                continue;
            }
            if (value instanceof JSONObject) { // application
                HSSFRow bodyRow = sheet.createRow(1);
                JSONObject jsonObject = (JSONObject) value;
                int i = 0;
                for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                    headRow.createCell(i).setCellValue(entry.getKey());
                    bodyRow.createCell(i).setCellValue(entry.getValue().toString());
                    sheet.autoSizeColumn(i);
                    i++;
                }
            }
            if (value instanceof JSONArray) {
                List<HashMap<String, String>> arrayList = this.parseArray((JSONArray) value);
                for (int i = 0; i < arrayList.size(); i++) {
                    HSSFRow bodyRow = sheet.createRow(i + 1);
                    int j = 0;
                    for (String name : arrayList.get(i).keySet()) {
                        headRow.createCell(j).setCellValue(name);
                        bodyRow.createCell(j).setCellValue(arrayList.get(i).get(name));
                        sheet.autoSizeColumn(j);
                        j++;
                    }
                }
            }
        }
        return workbook;
    }

    public static void main(String[] args) throws Exception {

        String inputFile = "D:\\mycode\\java\\my-demo\\data\\app-ng222.json";
        String outputFile = "D:\\mycode\\java\\my-demo\\data\\parse.xls";

        TestJson my = new TestJson();
        String content = my.readJson(inputFile);
        // 获取数据
        HSSFWorkbook workbook = my.getExcelData(content);
        FileOutputStream fOut = new FileOutputStream(outputFile);
        // 把相应的Excel 工作簿存盘
        workbook.write(fOut);
        fOut.flush();
        // 操作结束，关闭文件
        fOut.close();
        System.out.println("文件生成...");

    }




}
