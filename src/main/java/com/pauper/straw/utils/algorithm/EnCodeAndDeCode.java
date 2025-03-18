package com.pauper.straw.utils.algorithm;


import java.net.URLEncoder;
import java.util.Base64;

public class EnCodeAndDeCode {

    public static void main(String[] args) {
        String encoded = URLEncoder.encode("a中国");
        System.out.println(encoded);

        byte[] input = new byte[] { (byte) 0xe4, (byte) 0xb8, (byte) 0xad };
        String b64encoded = Base64.getEncoder().encodeToString(input);
        System.out.println(b64encoded);
    }


}
