package com.byteflow.learnffmpeg;

import android.Manifest;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.byteflow.learnffmpeg.view.RecordAudioButton;
import com.byteflow.learnffmpeg.view.RecordVoicePopWindow;
import com.byteflow.learnffmpeg.view.VideoAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import kr.co.namee.permissiongen.PermissionGen;

public class RecordActivity<T extends MainContract.Presenter> extends AppCompatActivity implements MainContract.View{

    private static final String LOG_TAG = "AudioRecordTest";
    //语音文件保存路径
    private String FileName = null;

    //界面控件
    private Button startRecord;
    private Button startPlay;
    private Button stopRecord;
    private Button stopPlay;
    private Button sys_record;

    //语音操作对象
    private MediaPlayer mPlayer = null;
    private MediaRecorder mRecorder = null;

    LinearLayout mRoot;
    RecyclerView mRvMsg;//消息列表
    RecordAudioButton mBtnVoice;//底部录制按钮

    private Context mContext;
    private VideoAdapter mAdapter = new VideoAdapter();//适配器
    private RecordVoicePopWindow mRecordVoicePopWindow;//提示
    private MainContract.Presenter mPresenter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mPresenter = new MainPresenter<MainContract.View>(this, this);
        setContentView(R.layout.activity_record);

        //开始录音
        startRecord = findViewById(R.id.startRecord);
        startRecord.setText(R.string.startRecord);
        //绑定监听器
        startRecord.setOnClickListener((View.OnClickListener) new startRecordListener());

        //结束录音
        stopRecord = findViewById(R.id.stopRecord);
        stopRecord.setText(R.string.stopRecord);
        stopRecord.setOnClickListener((View.OnClickListener) new stopRecordListener());

        //开始播放
        startPlay = findViewById(R.id.startPlay);
        startPlay.setText(R.string.startPlay);
        //绑定监听器
        startPlay.setOnClickListener((View.OnClickListener) new startPlayListener());

        //结束播放
        stopPlay = findViewById(R.id.stopPlay);
        stopPlay.setText(R.string.stopPlay);
        stopPlay.setOnClickListener((View.OnClickListener) new stopPlayListener());

        //设置sdcard的路径
        FileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        FileName += "/audioRecordtest.3gp";

        sys_record = findViewById(R.id.sys_record);
        sys_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.launchApp("com.android.soundrecorder");
            }
        });

        requestPermission();//请求麦克风权限
        initView();//初始化布局
        mPresenter.init();
    }


    //开始录音
    class startRecordListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(FileName);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            try {
                ToastUtils.showLong("开始录音中....");
                mRecorder.prepare();
            } catch (IOException e) {
                Log.e(LOG_TAG, "prepare() failed");
            }
            mRecorder.start();
        }

    }

    //停止录音
    class stopRecordListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try {
                if (mRecorder!=null){
                    ToastUtils.showLong("停止录音....");
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;
                }else {
                    ToastUtils.showLong("当前没有录音,无法停止,请先录音");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    //播放录音
    class startPlayListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mPlayer = new MediaPlayer();
            try{
                ToastUtils.showLong("播放录音....");
                mPlayer.setDataSource(FileName);
                mPlayer.prepare();
                mPlayer.start();
            }catch(IOException e){
                ToastUtils.showLong("播放失败:"+e.toString());
                Log.e(LOG_TAG,"播放失败:"+e.toString());
            }
        }

    }
    //停止播放录音
    class stopPlayListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try {
                if (mPlayer!=null){
                    if (mPlayer.isPlaying()){
                        ToastUtils.showLong("停止播放录音");
                        mPlayer.release();
                        mPlayer = null;
                    }else {
                        ToastUtils.showLong("当前没有正在播放的录音,无法停止播放,请先播放录音");
                    }
                }else {
                    ToastUtils.showLong("录音不存在，无法停止播放,请先播放录音");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private void initView() {
        mRoot = findViewById(R.id.root);
        mRvMsg = findViewById(R.id.rvMsg);
        mBtnVoice = findViewById(R.id.btnVoice);
        mBtnVoice.setOnVoiceButtonCallBack(new RecordAudioButton.OnVoiceButtonCallBack() {
            @Override
            public void onStartRecord() {
                mPresenter.startRecord();
            }

            @Override
            public void onStopRecord() {
                mPresenter.stopRecord();
            }

            @Override
            public void onWillCancelRecord() {
                mPresenter.willCancelRecord();
            }

            @Override
            public void onContinueRecord() {
                mPresenter.continueRecord();
            }
        });
        mRvMsg.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (R.id.iv_voice == view.getId()) {
                    mPresenter.startPlayRecord(position);
                }
            }
        });
        mRvMsg.setAdapter(mAdapter);
    }

    private void requestPermission() {
        PermissionGen.with(this)
                .addRequestCode(100)
                .permissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.WAKE_LOCK)
                .request();
    }

    @Override
    public void showList(List<File> list) {
        mAdapter.setNewData(list);
    }

    @Override
    public void showNormalTipView() {
        if (mRecordVoicePopWindow == null) {
            mRecordVoicePopWindow = new RecordVoicePopWindow(mContext);
        }
        mRecordVoicePopWindow.showAsDropDown(mRoot);
    }

    @Override
    public void showTimeOutTipView(int remainder) {
        if (mRecordVoicePopWindow != null) {
            mRecordVoicePopWindow.showTimeOutTipView(remainder);
        }
    }

    @Override
    public void showRecordingTipView() {
        if (mRecordVoicePopWindow != null) {
            mRecordVoicePopWindow.showRecordingTipView();
        }
    }

    @Override
    public void showRecordTooShortTipView() {
        if (mRecordVoicePopWindow != null) {
            mRecordVoicePopWindow.showRecordTooShortTipView();
        }
    }

    @Override
    public void showCancelTipView() {
        if (mRecordVoicePopWindow != null) {
            mRecordVoicePopWindow.showCancelTipView();
        }
    }

    @Override
    public void hideTipView() {
        if (mRecordVoicePopWindow != null) {
            mRecordVoicePopWindow.dismiss();
        }
    }

    @Override
    public void updateCurrentVolume(int db) {
        if (mRecordVoicePopWindow != null) {
            mRecordVoicePopWindow.updateCurrentVolume(db);
        }
    }

    @Override
    public void startPlayAnim(int position) {
        mAdapter.startPlayAnim(position);
    }

    @Override
    public void stopPlayAnim() {
        mAdapter.stopPlayAnim();
    }
}