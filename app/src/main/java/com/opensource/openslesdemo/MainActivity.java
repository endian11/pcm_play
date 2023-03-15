package com.opensource.openslesdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.opensource.openslesdemo.databinding.ActivityMainBinding;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "OpenSlEs";
    private OpenSlEsPlayer openSlEsPlayer;
    private AudioTrackPlayer audioTrackPlayer;
    private MediaPlayer mediaPlayer;
    private LinkedBlockingDeque<Data> deque1 = new LinkedBlockingDeque<>();
    private LinkedBlockingDeque<Data> deque2 = new LinkedBlockingDeque<>();
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
    private volatile boolean stop;

    private static class Data implements Serializable {
        public byte[] data;
        public int size;

        public Data(byte[] data, int size) {
            this.data = data;
            this.size = size;
        }
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadData();

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.reset();
            AssetManager mAssetManager = getAssets();
            AssetFileDescriptor mAssetFileDescriptor = mAssetManager.openFd("audio.wav");
            mediaPlayer.setDataSource(mAssetFileDescriptor.getFileDescriptor(),
                    mAssetFileDescriptor.getStartOffset(), mAssetFileDescriptor.getLength());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        openSlEsPlayer = new OpenSlEsPlayer();
        openSlEsPlayer.init();

        audioTrackPlayer = new AudioTrackPlayer();
        audioTrackPlayer.initAudioTrack();
    }

    public void opensles(View view) {
//        mediaPlayer.start();
//        mediaPlayer.setOnCompletionListener(mp -> play(1));

        play(1);
    }

    public void audioTrack(View view) {
//        mediaPlayer.start();
//        mediaPlayer.setOnCompletionListener(mp -> play(2));

        play(2);
    }

    private void loadData() {
        fixedThreadPool.submit(() -> {
            try {
                // mix.pcm是采样率44100 双声道 采样位数16位+
                String path =  "/sdcard/1678788405459.pcm";
                InputStream in = new FileInputStream(path);
//        AssetManager assetManager = getAssets();
//                InputStream in = assetManager.open("one.pcm");
                int n = 0;
                while (true) {
                    byte[] buffer = new byte[32000 * 2 * 2];
                    n = in.read(buffer);
                    if (n == -1) {
                        break;
                    }
                    Data data = new Data(buffer, n);
                    deque1.add(data);
                    deque2.add(data);
                }
                in.close();
                Log.d(TAG, "loadData read done!");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "loadData Exception ", e);
            }
        });
    }

    private void play(int type) {
        if (type == 1) {
            fixedThreadPool.submit((Runnable) () -> {
                while (!stop && deque2.size() > 0) {
                    try {
                        Data data = deque2.poll(100, TimeUnit.MILLISECONDS);
                        if (data == null) {
                            continue;
                        }
                        openSlEsPlayer.sendPcmData(data.data, data.size);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "openSlEsPlayer Exception ", e);
                    }
                }
                Log.e(TAG, "openSlEsPlayer done");
            });
        } else {
            fixedThreadPool.submit(() -> {
                while (!stop && deque1.size() > 0) {
                    try {
                        Data data = deque1.poll(100, TimeUnit.MILLISECONDS);
                        if (data == null) {
                            continue;
                        }
                        audioTrackPlayer.write(data.data, 0, data.size);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "audioTrackPlayer Exception ", e);
                    }
                }
                Log.e(TAG, "audioTrackPlayer done");
            });
        }
    }


    private AudioRecorder audioRecorder = new AudioRecorder();

    public void startRecord(View view) {
        audioRecorder.startRecord();
    }

    public void stopRecord(View view) {
        audioRecorder.stopRecord();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop = true;
        fixedThreadPool.shutdown();
        audioTrackPlayer.release();
        openSlEsPlayer.release();
        deque1.clear();
        deque2.clear();
        mediaPlayer.stop();
        mediaPlayer.release();
        audioRecorder.release();
    }
}