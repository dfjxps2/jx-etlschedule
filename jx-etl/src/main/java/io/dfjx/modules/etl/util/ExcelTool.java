package io.dfjx.modules.etl.util;


import io.dfjx.modules.etl.entity.ExlJobConfig;
import io.dfjx.modules.etl.entity.ExlJobDependency;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.sl.usermodel.FillStyle;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xwpf.usermodel.Borders;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * EXCEL报表工具类.
 *
 */
public class ExcelTool {

    /**
     * 创建表格标题
     * @param wb            Excel文档对象
     * @param sheet         工作表对象
     * @param headString    标题名称
     * @param col           标题占用列数
     */
    public static void createHeadTittle(HSSFWorkbook wb, HSSFSheet sheet, String headString, int col){
        HSSFRow row = sheet.createRow(0);           // 创建Excel工作表的行
        HSSFCell cell = row.createCell(0);          // 创建Excel工作表指定行的单元格
        row.setHeight((short) 1000);                // 设置高度

//        cell.setCellType(HSSFCell.ENCODING_UTF_16); // 定义单元格为字符串类型
        cell.setCellValue(new HSSFRichTextString(headString));

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col));  // 指定标题合并区域

        // 定义单元格格式，添加单元格表样式，并添加到工作簿
        HSSFCellStyle cellStyle = wb.createCellStyle();
//        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);             // 指定单元格居中对齐
//        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  // 指定单元格垂直居中个对齐
        cellStyle.setWrapText(true);                                    // 指定单元格自动换行

        // 设置单元格字体
        HSSFFont font = wb.createFont();
//        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 16); // 字体大小

        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
    }

    /**
     * 创建表头
     * @param wb            Excel文档对象
     * @param sheet         工作表对象
     * @param thead         表头内容
     * @param sheetWidth    每一列宽度
     */
    public static void createThead(HSSFWorkbook wb,HSSFSheet sheet,String[] thead,int[] sheetWidth){
        HSSFRow row1 = sheet.createRow(0);
        row1.setHeight((short) 600);
        // 定义单元格格式，添加单元格表样式，并添加到工作簿
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        cellStyle.setFillForegroundColor((short) 13);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
        cellStyle.setBorderTop(BorderStyle.THIN);//上边框
        cellStyle.setBorderRight(BorderStyle.THIN);//右边框
        cellStyle.setWrapText(true);
//        cellStyle.setTopBorderColor();


        // 设置单元格字体
        HSSFFont font = wb.createFont();
        font.setBold(true);
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 10);
//        font.setColor( );
        cellStyle.setFont(font);

        // 设置表头内容
        for(int i=0;i<thead.length;i++){
            HSSFCell cell1 = row1.createCell(i);
//            cell1.setCellType(HSSFCell.ENCODING_UTF_16);
            cell1.setCellValue(new HSSFRichTextString(thead[i]));
            cell1.setCellStyle(cellStyle);
        }

        // 设置每一列宽度
        for(int i=0;i<sheetWidth.length;i++){
            sheet.setColumnWidth(i, sheetWidth[i]);
        }
    }

    /**
     * 填入数据
     * @param wb        // Excel文档对象
     * @param sheet     // 工作表对象
     * @param result    // 表数据
     */
    public static void createTable(HSSFWorkbook wb,HSSFSheet sheet,List<Map<String, String>> result){
        // 定义单元格格式，添加单元格表样式，并添加到工作薄
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
        cellStyle.setBorderTop(BorderStyle.THIN);//上边框
        cellStyle.setBorderRight(BorderStyle.THIN);//右边框

        cellStyle.setWrapText(true);

        // 单元格字体
        HSSFFont font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 10);
        cellStyle.setFont(font);

        // 循环插入数据
        for(int i = 0; i < result.size(); i++ ){
            HSSFRow row = sheet.createRow(i+1);
            row.setHeight((short) 300); // 设置高度
            HSSFCell cell = null;

            int j = 0;
            for (String key : (result.get(i).keySet())) {
                cell = row.createCell(j);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new HSSFRichTextString(result.get(i).get(key)));
                j++;
            }
        }



    }

    public static List<Map<String, String>> createJobConfigDataSet(List<ExlJobConfig> exlJobConfigList){
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        Map<String, String> map;
        for (ExlJobConfig exlJobConfig : exlJobConfigList){
            map = new LinkedHashMap<>();
            map.put("ETL_System" ,exlJobConfig.getEtlSystem());
            map.put("ETL_Job"    ,exlJobConfig.getEtlJob());
            map.put("ETL_Server" ,exlJobConfig.getEtlServer());
            map.put("Description",exlJobConfig.getDescription());
            map.put("Frequency"  ,exlJobConfig.getFrequency());
            map.put("ScriptFile" ,exlJobConfig.getScriptFile());
            map.put("ScriptID"   ,exlJobConfig.getScriptID());
            map.put("ScriptType" ,exlJobConfig.getScriptType());
            map.put("EnableFlag" ,exlJobConfig.getEnableFlag());
            result.add(map);
        }

        return result;
    }


    public static List<Map<String, String>> createJobDependencyDataSet(List<ExlJobDependency> exlJobDependencyList){
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        Map<String, String> map;
        for (ExlJobDependency exlJobDependency : exlJobDependencyList){
            map = new LinkedHashMap<>();
            map.put("ETL_System" ,exlJobDependency.getETLSystem());
            map.put("ETL_Job"    ,exlJobDependency.getETLJob());
            map.put("Dependency_System" ,exlJobDependency.getDependencySystem());
            map.put("Dependency_Job",exlJobDependency.getDependencyJob());
            map.put("Trigger_Job_Flag"  ,exlJobDependency.getTriggerJobFlag());
            map.put("EnableFlag",exlJobDependency.getEnableFlag());
            result.add(map);
        }

        return result;
    }

    public static void main(String[] args) {
        String time = String.valueOf(new Date().getTime());
        String fileName = "job_batch_config_"+time+".xls";        // 定义文件名
        String headString = "公司员工信息表";          // 定义表格标题
        String sheetName1 = "jobconfig";                  // 定义工作表表名
        String filePath = "D:\\test\\";             // 文件本地保存路径
        String[] thead = {"ETL_System\n(作业系统)","ETL_Job\n(作业名称)","ETL_Server\n(ETL服务器)","Description\n(作业描述)","Frequency\n(作业执行周期)","ScriptFile\n(作业模板脚本名称)","ScriptID\n(作业模板ID)","ScriptType\n(作业模板类型)"};                    // 定义表头内容
        int[] sheetWidth = {5000,5000,5000,8000,5000,5000,5000,5000};   // 定义每一列宽度

        HSSFWorkbook wb = new HSSFWorkbook();           // 创建Excel文档对象
        HSSFSheet sheet = wb.createSheet(sheetName1);    // 创建工作表
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        Map<String, String> map = new LinkedHashMap<>();
        map.put("ETL_System" ,"PDT");
        map.put("ETL_Job"    ,"BATCH_TEST1");
        map.put("ETL_Server" ,"etl1");
        map.put("Description","批量部署作业测试程序1");
        map.put("Frequency"  ,"0");
        map.put("ScriptFile" ,"test1.sh");
        map.put("ScriptID"   ,"1");
        map.put("ScriptType" ,"sh");
        result.add(map);

//        Exl_Job_Config exlJobConfig = new Exl_Job_Config();
//        exlJobConfig.setETL_Server("PDT","BATCH_TEST1","etl1","批量部署作业测试程序1","0","test1.sh","1","sh");

        //创建表格标题
//        ExcelUtil.createHeadTittle(wb, sheet, headString, result.get(0).size() - 1);
        // result.get(0).size() - 1为表格占用列数，从0开始

        //创建表头
        ExcelTool.createThead(wb, sheet, thead, sheetWidth);

        //填入数据
        ExcelTool.createTable(wb, sheet, result);
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(new File(filePath+fileName));
//            wb.write(fos);
//            fos.close();
//            wb.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }
}
