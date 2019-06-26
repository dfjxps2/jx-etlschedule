package io.dfjx.modules.etl.util;


import java.io.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.dfjx.modules.etl.service.JobService;
import io.dfjx.modules.etl.service.impl.JobServiceImpl;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * 解析excel 上传数据
 *
 */
public class ExcelData {

    public static List<String[]> getSheetDataBySheetName(File file,String sheetName){

        FileInputStream input = null;
        List<String[]> list = new ArrayList<String[]>();
        try {
            input = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", IOUtils.toByteArray(input));
            list = ExcelData.getSheetDataByMultiAndSheetName(multipartFile,sheetName);
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static List<String[]> getSheetDataByMultiAndSheetName(MultipartFile file,String sheetName) throws IOException{
        checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(file);
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<String[]> list = new ArrayList<String[]>();
        if(workbook != null){
                //获得当前sheet工作表
                Sheet sheet = workbook.getSheet(sheetName);
                if(sheet == null){
                    return list;
                }
                //获得当前sheet的开始行
                int firstRowNum  = sheet.getFirstRowNum();
                //获得当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();
                //循环除了第一行的所有行
                for(int rowNum = firstRowNum+1;rowNum <= lastRowNum;rowNum++){
                    //获得当前行
                    Row row = sheet.getRow(rowNum);
                    if(row == null || row.getCell(1).toString() == ""){
                        return list;
                    }
                    //获得当前行的开始列
                    int firstCellNum = row.getFirstCellNum();
                    //获得当前行的列数
                    int lastCellNum = row.getLastCellNum();
                    String[] cells = new String[row.getLastCellNum()];
                    //循环当前行
                    for(int cellNum = firstCellNum; cellNum < lastCellNum;cellNum++){
                        Cell cell = row.getCell(cellNum);
                        cell.setCellType(CellType.STRING);
//                        cells[cellNum] = getCellValue(cell);
                        cells[cellNum] = String.valueOf(cell.getStringCellValue().trim());
                    }
                    list.add(cells);
            }

        }

        workbook.close();

        return list;
    }








    /**
     * 检查文件
     * @param file
     * @throws IOException
     */
    public static  void checkFile(MultipartFile file) throws IOException{
        //判断文件是否存在
        if(null == file){
            System.out.println("文件不存在！");
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if(!fileName.endsWith("xls") && !fileName.endsWith("xlsx")){
            System.out.println(fileName + "不是excel文件");
        }
    }


    public static  Workbook getWorkBook(MultipartFile file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if(fileName.endsWith("xls")){
                //2003
                workbook = new HSSFWorkbook(is);
            }else if(fileName.endsWith("xlsx")){
                //2007 及2007以上
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return workbook;
    }

//    private static final Logger log = Logger.getLogger(ExcelData.class);
//    public static List<String[]> getExcelData(MultipartFile file) throws IOException{
//        checkFile(file);
//        //获得Workbook工作薄对象
//        Workbook workbook = getWorkBook(file);
//        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
//        List<String[]> list = new ArrayList<String[]>();
//        if(workbook != null){
//            for(int sheetNum = 0;sheetNum < workbook.getNumberOfSheets();sheetNum++){
//                //获得当前sheet工作表
//                Sheet sheet = workbook.getSheetAt(sheetNum);
//                if(sheet == null){
//                    continue;
//                }
//                //获得当前sheet的开始行
//                int firstRowNum  = sheet.getFirstRowNum();
//                //获得当前sheet的结束行
//                int lastRowNum = sheet.getLastRowNum();
//                //循环除了第一行的所有行
//                for(int rowNum = firstRowNum+1;rowNum <= lastRowNum;rowNum++){
//                    //获得当前行
//                    Row row = sheet.getRow(rowNum);
//                    if(row == null){
//                        continue;
//                    }
//                    //获得当前行的开始列
//                    int firstCellNum = row.getFirstCellNum();
//                    //获得当前行的列数
//                    int lastCellNum = row.getLastCellNum();
//                    String[] cells = new String[row.getLastCellNum()];
//                    //循环当前行
//                    for(int cellNum = firstCellNum; cellNum < lastCellNum;cellNum++){
//                        Cell cell = row.getCell(cellNum);
////                        cells[cellNum] = getCellValue(cell);
//                        cells[cellNum] = String.valueOf(cell.getStringCellValue());
//                    }
//                    list.add(cells);
//                }
//            }
//        }
//        return list;
//    }
//    public static String getCellValue(HSSFCell cell) {
//        String cellValue = "";
//        DecimalFormat df = new DecimalFormat("#");
//        switch (cell.getCellType()) {
//            case HSSFCell.CELL_TYPE_STRING:
//                cellValue = cell.getRichStringCellValue().getString().trim();
//                break;
//            case HSSFCell.CELL_TYPE_NUMERIC:
//                cellValue = df.format(cell.getNumericCellValue()).toString();
//                break;
//            case HSSFCell.CELL_TYPE_BOOLEAN:
//                cellValue = String.valueOf(cell.getBooleanCellValue()).trim();
//                break;
//            case HSSFCell.CELL_TYPE_FORMULA:
//                cellValue = cell.getCellFormula();
//                break;
//            default:
//                cellValue = "";
//        }
//        return cellValue;
//    }

//    public static String getCellValue(Cell cell){
//        String cellValue = "";
//        if(cell == null){
//            return cellValue;
//        }
//        //判断数据的类型
//        switch (cell.getCellType()){
//            case Cell.CELL_TYPE_NUMERIC: //数字
////            case CellType.NUMERIC: //数字
//                cellValue = stringDateProcess(cell);
//                break;
//            case Cell.CELL_TYPE_STRING: //字符串
//                cellValue = String.valueOf(cell.getStringCellValue());
//                break;
//            case Cell.CELL_TYPE_BOOLEAN: //Boolean
//                cellValue = String.valueOf(cell.getBooleanCellValue());
//                break;
//            case Cell.CELL_TYPE_FORMULA: //公式
//                cellValue = String.valueOf(cell.getCellFormula());
//                break;
//            case Cell.CELL_TYPE_BLANK: //空值
//                cellValue = "";
//                break;
//            case Cell.CELL_TYPE_ERROR: //故障
//                cellValue = "非法字符";
//                break;
//            default:
//                cellValue = "未知类型";
//                break;
//        }
//        return cellValue;
//    }
//
//
//    /**
//     * 时间格式处理
//     * @return
//     * @author Liu Xin Nan
//     * @data 2017年11月27日
//     */
//    public static String stringDateProcess(Cell cell){
//        String result = new String();
//        if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
//            SimpleDateFormat sdf = null;
//            if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
//                sdf = new SimpleDateFormat("HH:mm");
//            } else {// 日期
//                sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//            }
//            Date date = cell.getDateCellValue();
//            result = sdf.format(date);
//        } else if (cell.getCellStyle().getDataFormat() == 58) {
//            // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//            double value = cell.getNumericCellValue();
//            Date date = org.apache.poi.ss.usermodel.DateUtil
//                    .getJavaDate(value);
//            result = sdf.format(date);
//        } else {
//            double value = cell.getNumericCellValue();
//            CellStyle style = cell.getCellStyle();
//            DecimalFormat format = new DecimalFormat();
//            String temp = style.getDataFormatString();
//            // 单元格设置成常规
//            if (temp.equals("General")) {
//                format.applyPattern("#");
//            }
//            result = format.format(value);
//        }
//
//        return result;
//    }
//

    public static void main(String[] args) {
//        File file = new File("c://lwq/job_batch_config.xlsx");
//        FileInputStream input = null;
//        List<String[]> list = new ArrayList<String[]>();
//        try {
//            input = new FileInputStream(file);
//            MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", IOUtils.toByteArray(input));
//            list = ExcelData.getSheetDataByName(multipartFile,"jobconfig");
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        for(String[] strings : list){
//            System.out.println("--------------"+strings[1]);
//        }

//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        System.out.println(dateFormat.format(new Date()));

    }

}