package com.example.demo.test;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.util.ExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestExcel {

    public JSONObject readExcel(String fileToBeRead) {
        JSONObject jsonObject = new JSONObject();
        try {
            // 创建对Excel工作簿文件的引用
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(fileToBeRead));
            int sheetCount = workbook.getNumberOfSheets();
            for (int i = 0; i < sheetCount; i++) {
                HSSFSheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();
                int rowNum = sheet.getLastRowNum();
                int columnNum = sheet.getRow(0).getPhysicalNumberOfCells();
                //int columnNum = sheet.getRow(0).getLastCellNum();
                if ("application".equals(sheetName)) {
                    HashMap<String, String> data = new HashMap<>();
                    for (int j = 0; j < columnNum; j++) {
                        String key = sheet.getRow(0).getCell(j).getStringCellValue();
                        String value = sheet.getRow(1).getCell(j).getStringCellValue();
                        data.put(key, value);
                    }
                    jsonObject.put(sheetName, new JSONObject(data));
                    continue;
                }
                if ("endpoints".equals(sheetName)) {
                    List<String> dataList = new ArrayList<>();
                    for (int k = 0; k < rowNum; k++) {
                        String value = sheet.getRow(k+1).getCell(0).getStringCellValue();
                        dataList.add(value);
                    }
                    jsonObject.put(sheetName, dataList);
                    continue;
                }
                if (rowNum == 0) {
                    jsonObject.put(sheetName, new JSONArray());
                }
                else {
                    List<HashMap<String, String>> dataList = new ArrayList<>();
                    for (int k = 0; k < rowNum; k++) {
                        HashMap<String, String> data = new HashMap<>();
                        for (int j = 0; j < columnNum; j++) {
                            String key = sheet.getRow(0).getCell(j).getStringCellValue();
                            String value = sheet.getRow(k+1).getCell(j).getStringCellValue();
                            data.put(key, value);
                        }
                        dataList.add(data);
                    }
                    jsonObject.put(sheetName, dataList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(jsonObject.toString());
        return jsonObject;
    }

    // 后续构造处理
    public void afterDeal(JSONObject jsonObject) {
        // configFileManageInfo
        JSONObject configFileManageInfo = new JSONObject();
        configFileManageInfo.put("configFileMapping", jsonObject.getJSONArray("configFileMapping"));
        jsonObject.put("configFileManageInfo", configFileManageInfo);
        jsonObject.remove("configFileMapping");

        JSONObject result = new JSONObject();
        result.put("appInfo", jsonObject);
        System.out.println(result.toString());
    }

    public static void main(String[] args) {
        TestExcel my = new TestExcel();
        String outputFile = "D:\\mycode\\java\\my-demo\\data\\parse.xls";
        JSONObject res = my.readExcel(outputFile);
        my.afterDeal(res);
    }
}
