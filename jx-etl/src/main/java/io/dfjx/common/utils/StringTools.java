package io.dfjx.common.utils;

import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.UUID;

public class StringTools {

    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };


    public static String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }

    /**
     * Url编码
     * @param str
     * @return
     */
    public static String urlEncode(String str) {
        if(StringUtils.isEmpty(str))
            return str;
        try {
            return java.net.URLEncoder.encode(str, "UTF-8");
        } catch (Exception ex) {
            return str;
        }
    }
    /**
     * Url解码
     * @param str
     * @return
     */
    public static String urlDecode(String str) {
        if(StringUtils.isEmpty(str))
            return str;
        try {
            return java.net.URLDecoder.decode(str, "UTF-8");
        } catch (Exception ex) {
            return str;
        }
    }

    public static String mstr(Map<String, Object> params, String key){
        if(!params.containsKey(key))
            return "";
        Object o = params.get(key);
        return o == null ? "" : o.toString();
    }
}
