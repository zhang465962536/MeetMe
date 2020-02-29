package com.example.framework.utils;

import java.security.MessageDigest;

public class SHA1 {

    /**
     * 融云加密算法
     * @param data
     * @return
     */
    public static String sha1(String data){
        StringBuffer buf = new StringBuffer();
        try{
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(data.getBytes());
            byte[] bits = md.digest();
            //计算规则
            for(int i = 0 ; i < bits.length;i++){
                int a = bits[i];
                if(a<0) a+=256;
                if(a<16) buf.append("0");
                buf.append(Integer.toHexString(a));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return buf.toString();
    }
}
