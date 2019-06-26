package io.dfjx.modules.etl.util;

import java.io.File;
import java.io.IOException;

public class ChmodUtil {

    public static boolean addExt(File fileName){
        String mycmd = "chmod +x " + fileName;
        Process process = null;

        try {
            process = Runtime.getRuntime().exec(mycmd);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
