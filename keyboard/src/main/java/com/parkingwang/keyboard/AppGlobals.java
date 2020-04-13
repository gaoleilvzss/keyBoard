package com.parkingwang.keyboard;

import android.app.Application;

import java.lang.reflect.InvocationTargetException;

public class AppGlobals {
    private static Application sAp;
    public static Application get(){
        if(sAp==null){
            try {
                sAp = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null,(Object[])null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return sAp;
    }
}
