package edu.fudan.ringbell.media;

import android.media.MediaPlayer;

import java.io.File;
import java.io.IOException;

/**
 * Created by YI on 2017/12/27.
 */

public class MusicPlayer {
    //单例模式
    private static MusicPlayer instance;

    private MusicPlayer() {
    }

    public static synchronized MusicPlayer getInstance() {
        if (instance == null) {
            instance = new MusicPlayer();
        }
        return instance;
    }

    private MediaPlayer mediaPlayer = null;

    //初始化需要IO操作，所以需要进行异步操作，传入OnPreparedListener作为初始化结束后的第一个响应
    //推荐响应：将某些（比如播放）按钮enable
    public void init(String path, MediaPlayer.OnPreparedListener listener) {
        if (mediaPlayer == null) {
            destory();
        }
        mediaPlayer = new MediaPlayer();
        File file = new File(path);
        try {
            mediaPlayer.setDataSource(file.getPath());//设置播放音频文件的路径
            mediaPlayer.setOnPreparedListener(listener);
            mediaPlayer.prepareAsync();//mp就绪
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void init(File file, MediaPlayer.OnPreparedListener listener) {
        if (mediaPlayer != null) {
            mediaPlayer = new MediaPlayer();
        }
        try {
            mediaPlayer.setDataSource(file.getPath());//设置播放音频文件的路径
            mediaPlayer.setOnPreparedListener(listener);
            mediaPlayer.prepareAsync();//mp就绪
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        }
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public void seekTo(int mesc) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(mesc);
        }
    }

    public int getLength() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        } else {
            return 0;
        }
    }

    public int getCurrentPosition() {
    	if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    public void destory() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

