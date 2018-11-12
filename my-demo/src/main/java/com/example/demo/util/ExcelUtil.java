package com.example.demo.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

public class ExcelUtil {

    public void createExcel(String outputFile) {
        try {
            // 创建新的Excel 工作簿
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 在Excel工作簿中建一工作表，其名为缺省值
            // 如要新建一名为"效益指标"的工作表，其语句为：
            // HSSFSheet sheet = workbook.createSheet("效益指标");
            HSSFSheet sheet = workbook.createSheet();
            // 在索引0的位置创建行（最顶端的行）
            HSSFRow row = sheet.createRow(0);
            //在索引0的位置创建单元格（左上端）
            HSSFCell cell = row.createCell(0);
            // 定义单元格为字符串类型
            cell.setCellType(CellType.STRING);
            // 在单元格中输入一些内容
            cell.setCellValue("hello");

            HSSFCell cell1 = row.createCell(1);
            CreationHelper creationHelper = workbook.getCreationHelper();
            DataFormat dataFormat = creationHelper.createDataFormat();
            // 创建格式
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setDataFormat(dataFormat.getFormat("yyyy-mm-dd hh:mm:ss"));
            // 应用格式
            // 定义单元格为字符串类型
            cell1.setCellStyle(cellStyle);
            // 在单元格中输入一些内容
            cell1.setCellValue(new Date());

            // 其他数据格式
            row.createCell(2).setCellValue(new Date());
            row.createCell(3).setCellValue(true);

            // 新建一输出文件流
            FileOutputStream fOut = new FileOutputStream(outputFile);
            // 把相应的Excel 工作簿存盘
            workbook.write(fOut);
            fOut.flush();
            // 操作结束，关闭文件
            fOut.close();
            System.out.println("文件生成...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readExcel(String fileToBeRead) {
        try {
            // 创建对Excel工作簿文件的引用
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(fileToBeRead));
            // 创建对工作表的引用。
            // 本例是按名引用（让我们假定那张表有着缺省名"Sheet0"）
            HSSFSheet sheet = workbook.getSheet("Sheet0");
            // 也可用getSheetAt(int index)按索引引用，
            // 在Excel文档中，第一张工作表的缺省索引是0，
            // 其语句为：HSSFSheet sheet = workbook.getSheetAt(0);
            // 读取左上端单元
            HSSFRow row = sheet.getRow(0);
            HSSFCell cell = row.getCell(0);
            // 输出单元内容，cell.getStringCellValue()就是取所在单元的值
            System.out.println("左上端单元是： " + cell.getStringCellValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ExcelUtil my = new ExcelUtil();
        String outputFile = "D:\\ZCM\\demo\\data\\test.xls";
        my.createExcel(outputFile);
        //my.readExcel(outputFile);
    }
}

