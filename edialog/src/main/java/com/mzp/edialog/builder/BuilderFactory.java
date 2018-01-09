package com.mzp.edialog.builder;

import android.util.SparseArray;

/**
 * Created by MTM on 2018/1/3.
 *
 * @author MZP
 */

public class BuilderFactory {

    public static final int BUILDER_DIALOG = 0;
    public static final int BUILDER_TOAST = 1;
    public static final int BUILDER_MENU = 2;

    public static SparseArray<Builder> defaultStyleArr = new SparseArray<>(0);

    public static Builder get(int index) {
        return defaultStyleArr.get(index);
    }

    public static void put(int index, Builder builder) {
        defaultStyleArr.put(index, builder);
    }

}