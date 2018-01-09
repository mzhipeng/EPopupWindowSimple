package com.mzp.edialog.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by MTM on 2018/1/3.
 *
 * @author MZP
 */

public abstract class AbstractActLifeCallbacks implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        ActManager.sInstance.addActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        ActManager.sInstance.removeCurrentAct(activity);
    }

}