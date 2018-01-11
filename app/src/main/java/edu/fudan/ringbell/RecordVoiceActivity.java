package edu.fudan.ringbell;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import edu.fudan.ringbell.adapter.VoiceAdapter;
import edu.fudan.ringbell.entity.VoiceInfo;
import edu.fudan.ringbell.media.MusicRecorder;
import edu.fudan.ringbell.media.VoiceUtil;

/**
 * Created by niuzhenghao on 2017/11/27.
 */

public class RecordVoiceActivity extends AppCompatActivity {
    private ListView recordlist;                   //音乐列表
    private VoiceAdapter mAdapter;
    List<VoiceInfo> voiceInfos = null;
    private MusicRecorder recorder = null;
    private Context context = null;
    private long recordTime = 0;
    private TextView timeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordvoice);
        context = getApplicationContext();
        ImageView goback = findViewById(R.id.goback);
        recordlist = findViewById(R.id.recordvoice);
        //为ListView添加数据源
        voiceInfos = VoiceUtil.getVoiceInfos(this.getApplicationContext());  //获取歌曲对象集合
        mAdapter =  new VoiceAdapter(this,voiceInfos);
        recordlist.setAdapter(mAdapter);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.audio_record_start).setEnabled(true);
        findViewById(R.id.audio_record_stop).setEnabled(false);
        timeView = findViewById(R.id.audio_record_time);

        recorder = MusicRecorder.getInstance();

        findViewById(R.id.audio_record_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recorder.init(Environment.getDataDirectory().getPath());
                recorder.record();
                recordTime = 0;
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        recordTime++;
                        String time = recordTime / 60 + ":" + recordTime % 60;
                        timeView.setText(time);
                    }
                },0,1000);
            }
        });

        findViewById(R.id.audio_record_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recorder.stop();
                voiceInfos = VoiceUtil.getVoiceInfos(context);  //获取歌曲对象集合
                mAdapter =  new VoiceAdapter(context,voiceInfos);
                recordlist.setAdapter(mAdapter);
            }
        });
    }

    @Override
    protected void onDestroy() {
        recorder.stop();
        super.onDestroy();
    }
}
