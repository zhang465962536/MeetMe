package com.example.meetme;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;

import com.example.framework.base.BaseUIActivity;
import com.example.framework.utils.LogUtils;
import com.example.framework.manager.MediaPlayerManager;

public class MainActivity extends BaseUIActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MediaPlayerManager mediaPlayerManager = new MediaPlayerManager();
        AssetFileDescriptor fileDescriptor = getResources().openRawResourceFd(R.raw.guide);
        mediaPlayerManager.startPlay(fileDescriptor);

        mediaPlayerManager.setOnProgressListener(new MediaPlayerManager.OnMusicProgressListener() {
            @Override
            public void OnProgress(int progress, int pos) {
                LogUtils.e(pos + "%");
            }
        });
    }
}
