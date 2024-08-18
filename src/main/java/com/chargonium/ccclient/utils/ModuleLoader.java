package com.chargonium.ccclient.utils;

import java.lang.reflect.Method;

public class ModuleLoader {

    public void registerModule(Object instance) {
        try {
            Method method = instance.getClass().getMethod("main");

            method.invoke(instance);
        } catch (Exception e) {
        }
    }


    public void registerModule(Object instance, String MainMethodName) {
        try {
            Method method = instance.getClass().getMethod(MainMethodName);

            method.invoke(instance);
        } catch (Exception e) {
        }
    }

}