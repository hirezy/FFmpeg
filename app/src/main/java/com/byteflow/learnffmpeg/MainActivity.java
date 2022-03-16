
package com.byteflow.learnffmpeg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.byteflow.learnffmpeg.adapter.MyRecyclerViewAdapter;
import com.byteflow.learnffmpeg.media.FFMediaPlayer;
import com.byteflow.learnffmpeg.util.CommonUtils;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String[] REQUEST_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static  final String [] EXAMPLE_LIST = {
            "FFmpeg + ANativeWindow player",
            "FFmpeg + OpenGL ES player",
            "FFmpeg + OpenSL ES visual audio",
            "FFmpeg + OpenGL ES VR player",
            "FFmpeg + single video recorder",
            "FFmpeg + single audio recorder",
            "FFmpeg + AV recorder",
            "FFmpeg + stream media player",
            "FFmpeg + MediaCodec player",
            "FFmpeg + Camerax",
            "FFmpeg + Record",
    };

    private static final int FF_ANATIVE_WINDOWS_EXAMPLE = 0;
    private static final int FF_OPENGLES_EXAMPLE = 1;
    private static final int FF_OPENGLES_AUDIO_VISUAL_EXAMPLE = 2;
    private static final int FF_OPENGLES_VR_EXAMPLE = 3;
    private static final int FF_X264_VIDEO_RECORDER = 4;
    private static final int FF_FDK_AAC_AUDIO_RECORDER = 5;
    private static final int FF_AV_RECORDER = 6;
    private static final int FF_STREAM_MEDIA_PLAYER = 7;
    private static final int FF_MEDIACODEC_PLAYER = 8;
    private static final int FF_CAMERAX = 9;
    private static final int FF_RECORD = 10;

    private int mSampleSelectedIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView)findViewById(R.id.tip)).setText("点击右上角的功能选择框开启功能体验吧\n\nFFmpeg 版本和编译配置信息如下:\n\n\n\n" + FFMediaPlayer.GetFFmpegVersion());

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSampleSelectedIndex = -1;
        CommonUtils.copyAssetsDirToSDCard(this, "byteflow", "/sdcard");
        if (!hasPermissionsGranted(REQUEST_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, REQUEST_PERMISSIONS, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (!hasPermissionsGranted(REQUEST_PERMISSIONS)) {
                Toast.makeText(this, "We need the permission: WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_change_sample) {
            showSelectExampleDialog();
        }
        return true;
    }

    private void showSelectExampleDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View rootView = inflater.inflate(R.layout.sample_selected_layout, null);

        final AlertDialog dialog = builder.create();

        Button confirmBtn = rootView.findViewById(R.id.confirm_btn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        final RecyclerView resolutionsListView = rootView.findViewById(R.id.resolution_list_view);

        final MyRecyclerViewAdapter myPreviewSizeViewAdapter = new MyRecyclerViewAdapter(this, Arrays.asList(EXAMPLE_LIST));
        myPreviewSizeViewAdapter.setSelectIndex(mSampleSelectedIndex);
        myPreviewSizeViewAdapter.addOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int selectIndex = myPreviewSizeViewAdapter.getSelectIndex();
                myPreviewSizeViewAdapter.setSelectIndex(position);
                myPreviewSizeViewAdapter.safeNotifyItemChanged(selectIndex);
                myPreviewSizeViewAdapter.safeNotifyItemChanged(position);
                mSampleSelectedIndex = position;
                switch (position) {
                    case FF_ANATIVE_WINDOWS_EXAMPLE:
                        startActivity(new Intent(MainActivity.this, NativeMediaPlayerActivity.class));
                        break;
                    case FF_OPENGLES_EXAMPLE:
                        startActivity(new Intent(MainActivity.this, GLMediaPlayerActivity.class));
                        break;
                    case FF_OPENGLES_AUDIO_VISUAL_EXAMPLE:
                        startActivity(new Intent(MainActivity.this, AudioVisualMediaPlayerActivity.class));
                        break;
                    case FF_OPENGLES_VR_EXAMPLE:
                        startActivity(new Intent(MainActivity.this, VRMediaPlayerActivity.class));
                        break;
                    case FF_X264_VIDEO_RECORDER:
                        startActivity(new Intent(MainActivity.this, VideoRecorderActivity.class));
                        break;
                    case FF_FDK_AAC_AUDIO_RECORDER:
                        startActivity(new Intent(MainActivity.this, AudioRecorderActivity.class));
                        break;
                    case FF_AV_RECORDER:
                        startActivity(new Intent(MainActivity.this, AVRecorderActivity.class));
                        break;
                    case FF_STREAM_MEDIA_PLAYER:
                        startActivity(new Intent(MainActivity.this, StreamMediaPlayerActivity.class));
                        break;
                    case FF_MEDIACODEC_PLAYER:
                        startActivity(new Intent(MainActivity.this, FFMediaCodecPlayerActivity.class));
                        break;
                    case FF_CAMERAX:
                        startActivity(new Intent(MainActivity.this, CameraxActivity.class));
                        break;
                    case FF_RECORD:
                        startActivity(new Intent(MainActivity.this, RecordActivity.class));
                        break;
                        default:
                            break;
                }

                dialog.cancel();
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        resolutionsListView.setLayoutManager(manager);

        resolutionsListView.setAdapter(myPreviewSizeViewAdapter);
        resolutionsListView.scrollToPosition(mSampleSelectedIndex);

        dialog.show();
        dialog.getWindow().setContentView(rootView);

    }

    protected boolean hasPermissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
