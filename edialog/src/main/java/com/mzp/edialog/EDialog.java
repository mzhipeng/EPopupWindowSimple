package com.mzp.edialog;

import android.content.Context;

import com.mzp.edialog.builder.Builder;
import com.mzp.edialog.builder.BuilderDialog;
import com.mzp.edialog.builder.BuilderFactory;
import com.mzp.edialog.listener.EAlterListener;

import static com.mzp.edialog.builder.BuilderDialog.TYPE.HINT;

/**
 * Created by MTM on 2018/1/3.
 *
 * @author MZP
 */

public class EDialog {

    public static void init(Context context) {
        EUtil.init(context);
    }

    /**
     * 通过该方法动态改变全局对话框样式
     *
     * background
     * ---- image
     * ---- color
     *
     * color
     * ---- title text color
     * ---- message text color
     * ---- btn text color
     *
     * anim
     * ---- bottom
     * ---- splash
     * ---- trans
     *
     */
    public static void initDefaultBuilder(Builder builder) {
        if (builder != null) {
            if (builder instanceof BuilderDialog) {
                BuilderFactory.put(BuilderFactory.BUILDER_DIALOG, builder);
            }
        }
    }

    public static BuilderDialog buildAlter(CharSequence title, CharSequence message, EAlterListener eAlterListener) {
        return new BuilderDialog(HINT).title(title).message(message).listener(eAlterListener).setDefaultStyle();
    }

}