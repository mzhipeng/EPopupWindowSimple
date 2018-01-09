package com.mzp.edialog.listener;

/**
 * Created by MTM on 2018/1/3.
 *
 * @author MZP
 */

public interface EAlterListener extends BaseListener {

    /**
     * right, 对话框右边数第1个
     */
    void onFirst();

    /**
     * mid,   对话框右边数第2个
     */
    void onSecond();

    /**
     * left,  对话框右边数第3个
     */
    void onThird();

}