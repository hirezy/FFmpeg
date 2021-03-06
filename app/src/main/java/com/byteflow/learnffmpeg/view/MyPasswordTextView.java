package com.byteflow.learnffmpeg.view;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;


public class MyPasswordTextView extends AppCompatTextView {
    private String content = "";//显示的内容
    //文本改变事件回调接口
    private OnMyTextChangedListener onMyTextChangedListener;
    /**
     * Handler线程对象,用来更新密码框的显示内容
     * 实现将输入的内容显示为密文
     */
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            //密文显示
            MyPasswordTextView.this.setSelected(true);
            //回调改变事件接口
            if (onMyTextChangedListener != null) {
                onMyTextChangedListener.textChanged(content);
            }
        }

        ;
    };

    /**
     * 构造方法
     *
     * @param context
     * @param attrs
     */
    public MyPasswordTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置文本改变事件监听
     *
     * @param onMyTextChangedListener
     */
    public void setOnMyTextChangedListener(OnMyTextChangedListener onMyTextChangedListener) {
        this.onMyTextChangedListener = onMyTextChangedListener;
    }

    /**
     * 设置密码框显示的内容
     *
     * @param text
     */
    public void setTextContent(String text) {
        //获得输入的内容
        this.content = text;
        if (!TextUtils.isEmpty(text)) {
            handler.sendEmptyMessage(0);//向Handler发送消息
        } else {
            this.setSelected(false);
        }
    }

    /**
     * 获取显示的内容
     *
     * @return
     */
    public String getTextContent() {
        return content;
    }

    /**
     * 文本改变事件接口
     */
    public interface OnMyTextChangedListener {
        /**
         * 密码框文本改变时调用
         *
         * @param content
         */
        public void textChanged(String content);
    }

}