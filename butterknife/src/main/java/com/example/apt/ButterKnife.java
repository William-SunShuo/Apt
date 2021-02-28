package com.example.apt;

import android.app.Activity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ButterKnife {

    public static Unbinder bind(Activity activity) {
        String className = activity.getClass().getName() + "_ViewBinding";
        try {
            Class instance = Class.forName(className);
            Constructor<? extends Unbinder> constructor = instance.getDeclaredConstructor(activity.getClass());
            return constructor.newInstance(activity);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return Unbinder.EMPTY;
    }
}
