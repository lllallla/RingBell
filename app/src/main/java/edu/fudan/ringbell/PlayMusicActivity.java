package edu.fudan.ringbell;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import edu.fudan.ringbell.entity.MusicInfo;
import edu.fudan.ringbell.media.MediaUtil;
import edu.fudan.ringbell.media.MusicPlayer;

/**
 * Created by niuzhenghao on 2018/1/7.
 */

public class PlayMusicActivity extends AppCompatActivity {
    private MusicPlayer player;
    private int musicPos;
    private List<MusicInfo> list;
    private boolean ready = false;
    private Drawable play;
    private Drawable pause;
    private SeekBar seekBar;
    private Timer timer;
    private boolean isSeekBarChanging;//互斥变量，防止进度条与定时器冲突。
    private Context context = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playmusic);
        context = this;
        ImageView goBack = findViewById(R.id.goback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        player = MusicPlayer.getInstance();
        Intent intent = getIntent();
        musicPos = intent.getIntExtra("pos",0);
        list = MediaUtil.getPreviousMusicInfos();
        ((TextView)findViewById(R.id.MusicName)).setText(list.get(musicPos).getTitle());
        player.init(list.get(musicPos).getUrl(), new MusicReadyListener());

        play = this.getResources().getDrawable(R.drawable.play_rdi_btn_play);
        pause = this.getResources().getDrawable(R.drawable.play_rdi_btn_pause);

        findViewById(R.id.playing_pre).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.destory();
                ready = false;
                musicPos--;
                if (musicPos < 0)
                    musicPos += list.size();
                player.init(list.get(musicPos).getUrl(), new MusicReadyListener());
            }
        });

        findViewById(R.id.playing_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.destory();
                ready = false;
                musicPos++;
                if (musicPos >= list.size())
                    musicPos -= list.size();
                player.init(list.get(musicPos).getUrl(), new MusicReadyListener());
            }
        });

        findViewById(R.id.playing_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ready) {
                    if (player.isPlaying()) {
                        player.pause();
                        timer.purge();
                        ((ImageView) v).setImageDrawable(play);
                    } else {
                        player.play();
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if(!isSeekBarChanging){
                                    seekBar.setProgress(player.getCurrentPosition());
                                }
                            }
                        },0,50);
                        ((ImageView) v).setImageDrawable(pause);
                    }
                }
            }
        });

        findViewById(R.id.playing_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MakeRIngActivity.class);
                intent.putExtra("path", list.get(musicPos).getUrl());
                startActivity(intent);
            }
        });

        //监听滚动条事件
        seekBar = findViewById(R.id.play_seek);
        seekBar.setMax((int)list.get(musicPos).getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBarListener());
    }

    @Override
    protected void onDestroy() {
        player.destory();
        timer.cancel();
        timer = null;
        super.onDestroy();
    }

    class MusicReadyListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            ready = true;
            int duration = mediaPlayer.getDuration();
            String current = duration / 60000 + ":" + (duration / 1000) % 60;
            ((TextView)findViewById(R.id.music_duration)).setText(current);
        }
    }

    class SeekBarListener implements SeekBar.OnSeekBarChangeListener {
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            String current = progress/60000 + ":" + (progress / 1000) % 60;
            ((TextView)findViewById(R.id.music_duration_played)).setText(current);
        }

        /*滚动时,应当暂停后台定时器*/
        public void onStartTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = true;
        }

        /*滑动结束后，重新设置值*/
        public void onStopTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = false;
            if (ready) {
                player.seekTo(seekBar.getProgress());
            }
        }
    }

}
