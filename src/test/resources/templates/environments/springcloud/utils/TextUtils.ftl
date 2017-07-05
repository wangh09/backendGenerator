package ${packageName};

import sun.misc.BASE64Decoder;

import java.security.MessageDigest;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by ${author} on ${date}.
 */
public class TextUtils {
    public static String passwdEncodeToDB(String s) {
        return md5(base64Decode(s));
    }

    private static BASE64Decoder base64Decoder = new BASE64Decoder();
    private static String base64Decode(String s) {
        String result = null;
        if (s != null) {
            try {
                byte[] b = base64Decoder.decodeBuffer(s);
                result = new String(b, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    private static String md5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String  getIdByUUID(){
        String id = UUID.randomUUID().toString();
        id=id.replaceAll("-", "");
        return id;
    }

    public static Date getNowTime() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT+8:00");
        Date dateTime = new Date();
        long chineseMills = dateTime.getTime() + timeZone.getRawOffset();
        return new Date(chineseMills);
    }
    static public String upperToLowerBegin(String camel) {
        return camel.substring(0, 1).toLowerCase() + camel.substring(1);
    }
    static public String lowerToUpperBegin(String camel) {
        return camel.substring(0, 1).toUpperCase() + camel.substring(1);
    }
    static public final String UNDERLINE = "_";
    public static String camelToUnderline(String param){
        if (param==null||"".equals(param.trim())){
            return "";
        }
        int len=param.length();
        StringBuilder sb=new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c=param.charAt(i);
            if (Character.isUpperCase(c)){
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }
}