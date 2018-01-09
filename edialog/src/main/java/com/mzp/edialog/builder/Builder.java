package com.mzp.edialog.builder;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.mzp.edialog.EUtil;
import com.mzp.edialog.R;

/**
 * Created by MTM on 2017-11-3.
 * 基本属性
 *
 * @author MZP
 */
public class Builder {

    public CharSequence title;
    public CharSequence message;

    public CharSequence btnText1;
    public CharSequence btnText2;
    public CharSequence btnText3;
    public @ColorRes
    int btnTextColor1 = R.color.hint_rad;
    public @ColorRes
    int btnTextColor2 = R.color.textBlack;
    public @ColorRes
    int btnTextColor3 = R.color.hint_blue;

    public @ColorRes
    int backgroundColor = R.color.white;
    public View contentView;
    public @LayoutRes
    int contentLayoutId;
    public int dialogGravity = Gravity.CENTER;

    PopupWindow dialog;

}