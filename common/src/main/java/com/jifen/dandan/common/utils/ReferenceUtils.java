package com.jifen.dandan.common.utils;

import java.lang.ref.Reference;

public class ReferenceUtils {

    private ReferenceUtils(){
    }

    public static boolean checkNull(Reference reference){
        return reference == null || reference.get() == null;
    }
}
