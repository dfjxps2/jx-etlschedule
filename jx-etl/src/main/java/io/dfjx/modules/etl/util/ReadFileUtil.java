package io.dfjx.modules.etl.util;

import java.io.*;

public class    ReadFileUtil {

//    public  static String readToString(String fileName) {
//        String encoding = "UTF-8";
//        File file = new File(fileName);
//        if (!file.exists()){
//            return "-1";
//        }
//        Long filelength = file.length();
//        byte[] filecontent = new byte[filelength.intValue()];
//        try {
//            FileInputStream in = new FileInputStream(file);
//            in.read(filecontent);
//            in.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            return new String(filecontent, encoding);
//        } catch (UnsupportedEncodingException e) {
//            System.err.println("The OS does not support " + encoding);
//            e.printStackTrace();
//            return null;
//        }
//    }

    public  static String readToString(String fileName) {
        File file = new File(fileName);
        if (!file.exists()){
            return "-1";
        }
        String encoding = "UTF-8";
//        String encoding = "ASCII";
        try {

            StringBuffer sb = new StringBuffer();

            FileReader reader = new FileReader(fileName);
            BufferedReader br = new BufferedReader(reader);

            String str;

            while ((str = br.readLine()) != null) {
                sb.append(str + "<br>");

            }

            br.close();
            reader.close();
//            return new String(sb.toString().getBytes(),encoding);
            System.out.println("sb.toString()=====" + sb.toString());
            if(sb.length()==0){
                return "-1";
            } else {
                String result_tmp = sb.toString().replaceAll("\t","&nbsp;&nbsp;&nbsp;&nbsp;");
                return new String(result_tmp.getBytes(),encoding);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
