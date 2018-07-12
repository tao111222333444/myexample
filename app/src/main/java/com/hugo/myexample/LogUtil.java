package com.hugo.myexample;


import android.util.Log;


/**
 * Created by 涛 on 2016/10/21.
 */

public class LogUtil {
    private static final boolean IS_LOG = BuildConfig.DEBUG;
    private static final String S_S="[";
    private static final String A_A = "]  ";
    public static void v(String method, String mess) {
        if (IS_LOG) { Log.v(getTag(),S_S+ method +A_A+  mess); }
    }
    public static void d(String method, String mess) {
        if (IS_LOG) { Log.d(getTag(),S_S+ method +A_A+  mess); }
    }
    public static void i(String method, String mess) {
        if (IS_LOG) { Log.i(getTag(),S_S+ method +A_A+  mess); }
    }
    public static void w(String method, String mess) {
        if (IS_LOG) { Log.w(getTag(),S_S+ method +A_A+  mess); }
    }
    public static void e(String method, String mess) {
        if (IS_LOG) { Log.e(getTag(),S_S+ method +A_A+ mess); }
    }

    /**
     * 获取到调用者的类名
     * @return
     */
    private static String getTag() {
        StackTraceElement[] trace = new Throwable().fillInStackTrace()
                .getStackTrace();
        String callingClass = "";
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(LogUtil.class)) {
                callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass
                        .lastIndexOf('.') + 1);
                break;
            }
        }
        return callingClass;
    }
}
