package com.chargonium.ccclient.utils;

import java.lang.reflect.Method;

public class ModuleLoader {

    public boolean registerModule(Object instance) {
        try {
            Method method = instance.getClass().getMethod("main");

            method.invoke(instance);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
