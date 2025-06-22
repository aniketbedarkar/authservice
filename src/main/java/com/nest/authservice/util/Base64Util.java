package com.nest.authservice.util;

import java.util.Base64;

public class Base64Util {

    private Base64Util(){
    }

    public static String getBase64DecodedPassword(String password){
        return new String(Base64.getDecoder().decode(password));
    }
}
