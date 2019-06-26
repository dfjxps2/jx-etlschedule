package io.dfjx.modules.etl.util;


import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class DownLoadFileUtil {

    /**
     * 下载项目根目录下doc下的文件
     *
     * @param response response
     * @param fileName 文件名
     * @return 返回结果 成功或者文件不存在
     */
//    public static String downloadFile(HttpServletResponse response, String path,String fileName) {
//        response.setHeader("content-type", "application/octet-stream");
//        response.setContentType("application/octet-stream");
//        response.setCharacterEncoding("UTF-8");
//        try {
//            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
//        } catch (UnsupportedEncodingException e2) {
//            e2.printStackTrace();
//        }
//        byte[] buff = new byte[1024];
//        BufferedInputStream bis = null;
//        OutputStream os;
//        try {
//            os = response.getOutputStream();
//            bis = new BufferedInputStream(new FileInputStream(new File(path + fileName)));
//            int i = bis.read(buff);
//            while (i != -1) {
//                os.write(buff, 0, buff.length);
//                os.flush();
//                i = bis.read(buff);
//            }
//        } catch (FileNotFoundException e1) {
//            e1.printStackTrace();
//            return "系统找不到指定的文件";
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (bis != null) {
//                try {
//                    bis.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return "success";
//
//    }

//    public static String downloadFile(HttpServletResponse response, String path,String fileName) {
//        File file = new File(path + fileName);
//        response.setContentType("application/octet-stream");
//        response.setCharacterEncoding("UTF-8");
//        try {
//            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
//        } catch (UnsupportedEncodingException e2) {
//            e2.printStackTrace();
//        }
//        try {
//            InputStream is = new FileInputStream(file);
//            OutputStream os=response.getOutputStream();
//            int data=0;
//            while ((data=is.read())!=-1){
//                os.write(data);
//            }
//            os.close();
//            is.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return "系统找不到指定的文件";
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//        return "success";
//
//    }


    public static String downloadFile(HttpServletResponse response, String path,String fileName) {
        File file = new File(path + fileName);
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if(suffix.equals("xls") || suffix.equals("xlsx")){
            response.setContentType("application/vnd.ms-excel; charset=utf-8");
        } else {
            response.setContentType("application/octet-stream");
        }

        //response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        try {
            InputStream is = new FileInputStream(file);
            OutputStream os=response.getOutputStream();
            int data=0;
            while ((data=is.read())!=-1){
                os.write(data);
            }
            os.close();
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "系统找不到指定的文件";
        } catch (IOException e){
            e.printStackTrace();
        }
        return "success";

    }
}




