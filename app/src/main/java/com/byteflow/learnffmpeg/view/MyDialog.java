package com.byteflow.learnffmpeg.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;

import com.blankj.utilcode.util.ToastUtils;
import com.byteflow.learnffmpeg.R;


public class MyDialog extends Dialog {

    private Button yes;//确定按钮
    private Button no;//取消按钮
    private TextView titleView;
    private AppCompatEditText messageView;

    private String title;


    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器
    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器

    public MyDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    // 设置确定按钮监听
    public void setYesOnclickListener(onYesOnclickListener yesOnclickListener) {
        this.yesOnclickListener = yesOnclickListener;
    }

    // 设置取消按钮监听
    public void setNoOnclickListener(onNoOnclickListener noOnclickListener) {
        this.noOnclickListener = noOnclickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_dialog);

        //初始化界面控件
        initView();

        //初始化界面数据
        initData();

        //初始化界面控件的事件
        initEvent();
    }

    // 初始化界面控件
    private void initView() {
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);
        titleView = findViewById(R.id.title);
        messageView = findViewById(R.id.message);
    }

    // 初始化界面控件的显示数据
    private void initData() {
        if (title != null) {
            titleView.setText(title);
        }
    }

    // 初始化界面的确定和取消按钮监听
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    String msg=messageView.getText().toString().trim();
                    if (TextUtils.isEmpty(msg)){
                        ToastUtils.showLong("密码为空!请重新输入");
                    }else {
                        if (msg.length()>4){
                            ToastUtils.showLong("密码有限长度为4位");
                        }else {
                            yesOnclickListener.onYesOnclick(msg);
                        }
                    }
                }
            }
        });

        //设置取消按钮被点击后，向外界提供监听
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoOnclick();
                }
            }
        });
    }

    // 设置标题
    public void setTitle(String title) {
        this.title = title;
    }


    public interface onYesOnclickListener {
        public void onYesOnclick(String pwd);
    }

    public interface onNoOnclickListener {
        public void onNoOnclick();
    }
}