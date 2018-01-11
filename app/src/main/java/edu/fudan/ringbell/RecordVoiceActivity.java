package edu.fudan.ringbell;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

import edu.fudan.ringbell.adapter.VoiceAdapter;
import edu.fudan.ringbell.entity.VoiceInfo;
import edu.fudan.ringbell.media.VoiceUtil;

/**
 * Created by niuzhenghao on 2017/11/27.
 */

public class RecordVoiceActivity extends AppCompatActivity {
    private ListView recordlist;                   //音乐列表
    private VoiceAdapter mAdapter;
    List<VoiceInfo> voiceInfos = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordvoice);
        ImageView goback = (ImageView) findViewById(R.id.goback);
        recordlist = (ListView) findViewById(R.id.recordvoice);
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

    }

}
