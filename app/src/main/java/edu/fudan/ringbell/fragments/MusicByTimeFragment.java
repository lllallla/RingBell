package edu.fudan.ringbell.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.fudan.ringbell.R;
import edu.fudan.ringbell.adapter.MusicAdapter;
import edu.fudan.ringbell.entity.MusicInfo;
import edu.fudan.ringbell.media.MediaUtil;

/**
 * Created by niuzhenghao on 2017/11/27.
 */

public class MusicByTimeFragment extends Fragment {
    private ListView mMusiclist;                   //音乐列表
    private MusicAdapter mAdapter;
    List<MusicInfo> musicInfos = null;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflater.inflate(resource, null);

        View view = inflater.inflate(R.layout.fragment_mr_time, container, false);
        mMusiclist = (ListView) view.findViewById(R.id.musiclistbytime);
        //为ListView添加数据源
        musicInfos = MediaUtil.getMusicInfos(getActivity().getApplicationContext());  //获取歌曲对象集合
        Comparator c = new Comparator<MusicInfo>() {
            @Override
            public int compare(MusicInfo m1, MusicInfo m2) {
                // TODO Auto-generated method stub
                if(m1.getDateModified()<m2.getDateModified())
                    return 1;
                    //注意！！返回值必须是一对相反数，否则无效。jdk1.7以后就是这样。
                    //      else return 0; //无效
                else return -1;
            }
        };
        Collections.sort(musicInfos,c);
        mAdapter =  new MusicAdapter(this.getActivity(),musicInfos);
        mMusiclist.setAdapter(mAdapter);
        return view;
    }

}
