package com.mzp.edialog.builder;

import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mzp.edialog.EUtil;
import com.mzp.edialog.R;
import com.mzp.edialog.app.ActManager;
import com.mzp.edialog.listener.BaseListener;
import com.mzp.edialog.listener.EAlterListener;

import static com.mzp.edialog.builder.BuilderFactory.BUILDER_DIALOG;

/**
 * Created by MTM on 2018/1/3.
 *
 * @author MZP
 */

public class BuilderDialog extends Builder {

    public BuilderDialog() {

    }

    public BuilderDialog(TYPE type) {
        this.type = type;
    }

    public BuilderDialog setDefaultStyle() {
        BuilderDialog builder = (BuilderDialog) BuilderFactory.get(BUILDER_DIALOG);
        if (builder != null) {
            btnText1 = builder.btnText1;
            btnText2 = builder.btnText2;
            btnText3 = builder.btnText3;
            btnTextColor1 = builder.btnTextColor1;
            btnTextColor2 = builder.btnTextColor2;
            btnTextColor3 = builder.btnTextColor3;
        }
        return this;
    }

    public enum TYPE {
        /**
         * 提示框
         */
        HINT,
        /**
         * 单选
         */
        CHOOSE,
        /**
         * 复选
         */
        CHECK_BOX
    }

    private void create() {
        dialog = new PopupWindow(initView(), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        setAnimationStyle();
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        dialog.setFocusable(true);
        // 响应点击事件
        dialog.setOutsideTouchable(true);
        // setBackgroundAlpha(0.5f, act);
        // 这是响应返回键让弹出消失
        dialog.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    /**
     * set PopupWindow ContentView
     *
     * @return contentView
     */
    private View initView() {
        switch (type) {
            case HINT:
                contentLayoutId = R.layout.include_dialog_message_tv;
                return initDefaultView();
            case CHOOSE:
                return initDefaultView();
            case CHECK_BOX:
                return initDefaultView();
            default:
                return initDefaultView();
        }
    }

    /**
     * set PopupWindow 动画样式
     */
    private void setAnimationStyle() {
        //底部弹出
//        dialog.setAnimationStyle(R.style.bottom);
        //右侧弹出
//        dialog.setAnimationStyle(R.style.bottom);
        //渐变弹出
//        dialog.setAnimationStyle(R.style.bottom);
        //过渡弹出
//        dialog.setAnimationStyle(R.style.bottom);
        //无
    }

    /**
     * set PopupWindow 依赖位置
     */
    private void setShowAtLocation() {
        dialog.showAtLocation(ActManager.sInstance.getTopActivity().getWindow().getDecorView(), dialogGravity, 0, 0);

    }

    public PopupWindow show() {
        create();
        // 异步显示对话框
        if (EUtil.isRunInMainThread()) {
            // 解决 is activity running 问题
            ActManager.sInstance.getTopActivity().getWindow().getDecorView().post(new Runnable() {

                @Override
                public void run() {
                    setShowAtLocation();
                }
            });
        } else {
            // 解决 子线程 show 问题
            EUtil.postMain(new Runnable() {

                @Override
                public void run() {
                    setShowAtLocation();
                }
            });
        }
        return dialog;
    }

    public void dismissPow() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    private View initDefaultView() {
        if (contentView == null) {
            contentView = EUtil.inflate(contentLayoutId);
        }
        View baseView = EUtil.inflate(R.layout.dialog_md);
        TextView mTitleTv = (TextView) baseView.findViewById(R.id.dialog_title_tv);
        Button mBtn1 = (Button) baseView.findViewById(R.id.dialog_btn1);
        Button mBtn2 = (Button) baseView.findViewById(R.id.dialog_btn2);
        Button mBtn3 = (Button) baseView.findViewById(R.id.dialog_btn3);
        final FrameLayout contentLayout = (FrameLayout) baseView.findViewById(R.id.dialog_content_fl);
        // 添加内容
        contentLayout.addView(contentView);

        mTitleTv.setText(title);

        TextView mMessageTv = (TextView) contentView.findViewById(R.id.message_tv);
        if (mMessageTv != null) {
            mMessageTv.setText(message);
        }

        if (!EUtil.isEmpty(btnText1)) {
            mBtn1.setVisibility(View.VISIBLE);
            mBtn1.setText(btnText1);
            EUtil.setTextColor(mBtn1, btnTextColor1);
            mBtn1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (listener != null && listener instanceof EAlterListener) {
                        ((EAlterListener) listener).onFirst();
                    }
                    dismissPow();
                }
            });
        }
        if (!EUtil.isEmpty(btnText2)) {
            mBtn2.setVisibility(View.VISIBLE);
            mBtn2.setText(btnText2);
            EUtil.setTextColor(mBtn2, btnTextColor2);
            mBtn2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (listener != null && listener instanceof EAlterListener) {
                        ((EAlterListener) listener).onSecond();
                    }
                    dismissPow();
                }
            });
        }

        if (!EUtil.isEmpty(btnText3)) {
            mBtn3.setVisibility(View.VISIBLE);
            mBtn3.setText(btnText3);
            EUtil.setTextColor(mBtn3, btnTextColor3);
            mBtn3.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (listener != null && listener instanceof EAlterListener) {
                        ((EAlterListener) listener).onThird();
                    }
                    dismissPow();
                }
            });
        }
        return baseView;
    }

    /**
     * 单选
     */
//    public View initSingleChoose() {
//        view = UIUtils.inflate(R.layout.dialog_md_single_choose);
//
//        TextView titleTv = (TextView) view.findViewById(R.id.title_tv);
//        titleTv.setText(title);
//        final TextView mChooseHintTv = (TextView) view
//                .findViewById(R.id.choose_hint_tv);
//        final CheckBox mChooseCbx = (CheckBox) view
//                .findViewById(R.id.choose_rb);
//        // 设置用户记录的状态
//        boolean saveAndUploadSupervise = SPUtils.i("Dialog").getBooleanData(
//                "saveAndUploadSupervise", false);
//        if (saveAndUploadSupervise) {
//            mChooseCbx.setChecked(saveAndUploadSupervise);
//        } else {
//            mChooseHintTv.setVisibility(View.GONE);
//        }
//
//        Button mSuccessBtn = (Button) view.findViewById(R.id.success_btn);
//
//        final Button mCancelBtn = (Button) view.findViewById(R.id.cancel_btn);
//        if (canChoose) {
//            mChooseCbx
//                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//                        @Override
//                        public void onCheckedChanged(CompoundButton buttonView,
//                                                     boolean isChecked) {
//                            if (listener != null
//                                    && listener instanceof OnSingleChooseListener) {
//                                ((OnSingleChooseListener) listener).onChoose(
//                                        buttonView, isChecked);
//                            }
//                            if (isChecked) {
//                                mChooseHintTv.setVisibility(View.VISIBLE);
//                            } else {
//                                mChooseHintTv.setVisibility(View.GONE);
//                            }
//                        }
//                    });
//        } else {
//            mChooseCbx.setChecked(true);
//            mChooseCbx.setEnabled(false);
//            mChooseHintTv.setVisibility(View.VISIBLE);
//            mChooseHintTv.setText(chooseHint);
//        }
//        mSuccessBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (listener != null) {
//                    listener.onFinish();
//                    mCancelBtn.setClickable(false);
//                }
//                dismissPow();
//            }
//        });
//        mCancelBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (listener != null) {
//                    listener.onCancel();
//                }
//                dismissPow();
//            }
//        });
//        return view;
//    }

    /**
     * 多选
     */


    //    private boolean canChoose = true;
//    private String[] radioItemValue;
//    private String[] radioItemKey;
//    private int radioChoosePosition = -1;

    private TYPE type;
    private BaseListener listener;

    public BuilderDialog listener(BaseListener listener) {
        this.listener = listener;
        return this;
    }

    public BuilderDialog message(CharSequence message) {
        this.message = message;
        return this;
    }

    public BuilderDialog message(@StringRes int messageRes) {
        this.message = EUtil.getStrRes(messageRes);
        return this;
    }

    public BuilderDialog title(CharSequence title) {
        this.title = title;
        return this;
    }

    public BuilderDialog contentView(@LayoutRes int contentLayoutId) {
        this.contentLayoutId = contentLayoutId;
        return this;
    }

    public BuilderDialog contentView(View contentView) {
        this.contentView = contentView;
        return this;
    }

    public BuilderDialog btnText(CharSequence btnText1) {
        return btnText(btnText1, null);
    }

    public BuilderDialog btnText(CharSequence btnText1, CharSequence btnText2) {
        return btnText(btnText1, btnText2, null);
    }

    public BuilderDialog btnText(CharSequence btnText1, CharSequence btnText2, CharSequence btnText3) {
        this.btnText1 = btnText1;
        this.btnText2 = btnText2;
        this.btnText3 = btnText3;
        return this;
    }

    public BuilderDialog btnTextColor(@ColorRes int btnTextColor1) {
        return btnTextColor(btnTextColor1, 0);
    }

    public BuilderDialog btnTextColor(@ColorRes int btnTextColor1, @ColorRes int btnTextColor2) {
        return btnTextColor(btnTextColor1, btnTextColor2, 0);
    }

    public BuilderDialog btnTextColor(@ColorRes int btnTextColor1, @ColorRes int btnTextColor2, @ColorRes int btnTextColor3) {
        this.btnTextColor1 = btnTextColor1;
        this.btnTextColor2 = btnTextColor2;
        this.btnTextColor3 = btnTextColor3;
        return this;
    }

}