package com.mzp.edialog;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mzp.edialog.builder.Builder;
import com.mzp.edialog.builder.BuilderDialog;
import com.mzp.edialog.builder.BuilderFactory;

import java.util.Collection;
import java.util.Map;

/**
 * Created by MTM on 2018/1/3.
 *
 * @author MZP
 */

public class EUtil {

    public static Context app;

    /**
     * 主线程ID
     */
    private static long mMainThreadId = -1;
    /**
     * 主线程Handler
     */
    private static Handler mMainThreadHandler;

    public static void init(Context context) {
        app = context;
        mMainThreadId = context.getMainLooper().getThread().getId();
        mMainThreadHandler = new Handler(Looper.getMainLooper());
    }

    public static boolean isRunInMainThread() {
        return android.os.Process.myTid() == getMainThreadId();
    }

    public static void runInMainThread(Runnable runnable) {
        if (isRunInMainThread()) {
            runnable.run();
        } else {
            postMain(runnable);
        }
    }

    public static long getMainThreadId() {
        return mMainThreadId;
    }

    public static void postMain(Runnable runnable) {
        mMainThreadHandler.post(runnable);
    }

    public static boolean postMain(Runnable runnable, long time) {
        return mMainThreadHandler.postDelayed(runnable, time);
    }

    public static boolean isEmpty(CharSequence str) {
        return isNull(str) || str.length() == 0 || "null".equals(str);
    }

    public static boolean isEmpty(Object[] obj) {
        return isNull(obj) || obj.length == 0;
    }

    public static boolean isEmpty(Collection<?> list) {
        return isNull(list) || list.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return isNull(map) || map.isEmpty();
    }

    public static boolean isNull(Object o) {
        return o == null;
    }

    public static CharSequence getStrRes(@StringRes int strResId) {
        return app.getResources().getString(strResId);
    }

    public static int getColorRes(@ColorRes int colorResId) {
        return ContextCompat.getColor(app, colorResId);
    }

    public static View inflate(Context context, @LayoutRes int layoutResId) {
        return LayoutInflater.from(context).inflate(layoutResId, null);
    }

    public static View inflate(@LayoutRes int layoutResId) {
        return LayoutInflater.from(app).inflate(layoutResId, null);
    }

    public static void setTextColor(TextView view, @ColorRes int textColor) {
        if (textColor > 0) {
            view.setTextColor(getColorRes(textColor));
        }
    }

    public static void setTextColor(EditText view, @ColorRes int textColor) {
        if (textColor > 0) {
            view.setTextColor(getColorRes(textColor));
        }
    }

    public static void setTextColor(Button view, @ColorRes int textColor) {
        if (textColor > 0) {
            view.setTextColor(getColorRes(textColor));
        }
    }

    public static void val14Val2(CharSequence val1, CharSequence val2) {
        if (!isEmpty(val2)) {
            val1 = val2;
        }
    }

    public static void val14Val2(int val1, int val2) {
        if (val2 > 0) {
            val1 = val2;
        }
    }

}