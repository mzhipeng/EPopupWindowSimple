package com.mzp.edialog.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.mzp.edialog.EUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by MTM on 2018/1/3.
 *
 * @author MZP
 */

public class ActManager {

    private static String TAG = "com.mzp.edialog.app.ActManager";
    public static ActManager sInstance = new ActManager();
    private static Stack<Activity> mActivityStack = new Stack<>();

    private ActManager() {
    }

    public Activity getTopActivity() {
        if (mActivityStack == null) {
            return null;
        }
        Activity mTopAct = null;
        if (mActivityStack.size() > 0) {
            mTopAct = mActivityStack.get(mActivityStack.size() - 1);
        } else {
            try {
                mTopAct = getTopActivity(getActivities());
            } catch (ReflectUtils.ReflectException e) {
                e.printStackTrace();
            }
        }
        return mTopAct;
    }

    void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        if (activity != null) {
            mActivityStack.add(activity);
        }
    }

    void removeCurrentAct(Activity activity) {
        mActivityStack.remove(activity);
    }


    /**
     * 获取当前App所有Activity的引用。
     * <p>
     * 注意：当前List是无序List，Activity在List中的排列顺序并不代表
     * Activity在ActivityStack中的顺序。如果需要获取栈顶Activity，请调用{@link #getTopActivity()}方法。
     *
     * @return Activity列表，获取不到的时候将返回null。
     * @throws ReflectUtils.ReflectException 可能会发生异常，强制要求处理异常情况。
     */
    public static List<Activity> getActivities() throws ReflectUtils.ReflectException {
        return getActivities(getActivitiesInActivityThread());
    }

    private static Object getActivitiesInActivityThread() throws ReflectUtils.ReflectException {
        return ReflectUtils.reflect(null, "android.app.ActivityThread#currentActivityThread().mActivities");
    }

    /**
     * 获取当前App所有Activity的引用。
     * <p>
     * 注意：当前List是无序List，Activity在List中的排列顺序并不代表
     * Activity在ActivityStack中的顺序。如果需要获取栈顶Activity，请调用{@link #getTopActivity()}方法。
     *
     * @param mActivities ActivityThread中的mActivities
     * @return Activity列表，获取不到的时候将返回null。
     * @throws ReflectUtils.ReflectException 可能会发生异常，强制要求处理异常情况。
     */
    public static List<Activity> getActivities(Object mActivities) throws ReflectUtils.ReflectException {
        if (mActivities != null) {
            return toActivityList(mActivities);
        }
        return null;
    }

    private static List<Activity> toActivityList(Object activities) throws ReflectUtils.ReflectException {
        if (activities == null) {
            return null;
        }
        List<Activity> list = new ArrayList<>();
        if (activities instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<Object, Object> arrayMap = (Map<Object, Object>) activities;
            for (Map.Entry<Object, Object> entry : arrayMap.entrySet()) {
                Object value = entry.getValue();
                Object o = ReflectUtils.reflect(value, "activity");
                list.add((Activity) o);
            }
        }
        return list;
    }

    /**
     * 获取栈顶Activity引用。
     *
     * @param activities 所有的Activity列表
     * @return 栈顶Activity，获取不到的时候将返回null。
     */
    public static Activity getTopActivity(List<Activity> activities) {
        try {
            Activity activity = getTopActivityByIsTopOfTask(activities);
            if (activity != null) {
                return activity;
            }
        } catch (ReflectUtils.ReflectException e) {
            e.printStackTrace();
        }
        try {
            Activity activity = getTopActivityByResume(activities);
            if (activity != null) {
                return activity;
            }
        } catch (ReflectUtils.ReflectException e) {
            e.printStackTrace();
        }

        try {
            Activity activity = getTopActivityByActivityManager(EUtil.app, activities);
            if (activity != null) {
                return activity;
            }
        } catch (ReflectUtils.ReflectException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过ActivityManager拿到栈顶Activity的ClassName，然后跟列表中的Activity比对，获取栈顶Activity。
     * 注意：这种方式有个很严重的缺陷，如果某个Activity在栈里多次出现将导致判断错误，这时候将返回null。
     * 可靠性相对一般，不会返回错误结果。
     *
     * @param application application
     * @param activities  所有的Activity列表
     * @return 栈顶Activity，获取不到的时候将返回null，不会返回错误结果。
     * @throws ReflectUtils.ReflectException 可能会发生异常，强制要求处理异常情况。
     */
    public static Activity getTopActivityByActivityManager(Context application
            , List<Activity> activities) throws ReflectUtils.ReflectException {
        ActivityManager activityManager = (ActivityManager) application
                .getSystemService(Context.ACTIVITY_SERVICE);
        String topActivity = null;
        String packageName = application.getPackageName();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<ActivityManager.AppTask> appTasks = activityManager.getAppTasks();
            for (int i = 0, size = appTasks.size(); i < size; i++) {
                ActivityManager.RecentTaskInfo taskInfo = appTasks.get(size - 1 - i).getTaskInfo();
                if (packageName.equals(taskInfo.baseActivity.getPackageName())) {
                    topActivity = taskInfo.topActivity.getClassName();
                }
            }
        }
        if (EUtil.isEmpty(topActivity)) {
            Log.i(TAG, "尝试通过getTopActivityByActivityManager获取Activity失败");
            return null;
        }

        boolean check = false;
        Activity result = null;
        if (activities != null) {
            for (Activity activity : activities) {
                if (topActivity.equals(activity.getClass().getName())) {
                    if (check) {
                        // 出现重复Activity，返回null
                        Log.i(TAG, "尝试通过getTopActivityByActivityManager获取Activity失败");
                        return null;
                    }
                    check = true;
                    result = activity;
                }
            }
        }
        if (result == null) {
            Log.w(TAG, "尝试通过getTopActivityByActivityManager获取Activity失败");
        } else {
            Log.i(TAG, "尝试通过getTopActivityByActivityManager获取Activity成功");
        }
        return result;
    }

    /**
     * 通过反射Activity的isTopOfTask方法来获取栈顶Activity引用。可靠性相对最好，不会返回错误结果。
     *
     * @param activities 所有的Activity列表
     * @return 栈顶Activity，获取不到的时候将返回null，不会返回错误结果。
     * @throws ReflectUtils.ReflectException 可能会发生异常，强制要求处理异常情况。
     */
    public static Activity getTopActivityByIsTopOfTask(List<Activity> activities)
            throws ReflectUtils.ReflectException {
        for (Activity activity : activities) {
            boolean isTop = (boolean) ReflectUtils.reflect(activity, "isTopOfTask()");
            if (isTop) {
                Log.i(TAG, "尝试通过getTopActivityByIsTopOfTask获取Activity成功");
                return activity;
            }
        }
        Log.w(TAG, "尝试通过getTopActivityByIsTopOfTask获取Activity失败");
        return null;
    }

    /**
     * 通过反射Activity的isTopOfTask方法来获取栈顶Activity引用。可靠性相对一般。
     *
     * @param activities 所有的Activity列表
     * @return 栈顶Activity，获取不到的时候将返回null，可能会返回错误结果。
     * @throws ReflectUtils.ReflectException 可能会发生异常，强制要求处理异常情况。
     */
    public static Activity getTopActivityByResume(List<Activity> activities)
            throws ReflectUtils.ReflectException {
        for (Activity activity : activities) {
            boolean isTop = (boolean) ReflectUtils.reflect(activity, "mResumed");
            if (isTop) {
                Log.i(TAG, "尝试通过getTopActivityByResume获取Activity成功");
                return activity;
            }
        }
        Log.w(TAG, "尝试通过getTopActivityByResume获取Activity失败");
        return null;
    }

}