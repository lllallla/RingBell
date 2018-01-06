package edu.fudan.ringbell.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;

import edu.fudan.ringbell.R;
import edu.fudan.ringbell.adapter.MusicAdapter;
import edu.fudan.ringbell.entity.MusicInfo;
import edu.fudan.ringbell.media.MediaUtil;

/**
 * Created by niuzhenghao on 2017/11/27.
 */

public class MusicByWordFragment extends Fragment {
    private String tag = "musicByword";
    private ListView mMusiclist;                   //音乐列表
    private MusicAdapter mAdapter;
    List<MusicInfo> musicInfos = null;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mr_word, container, false);
        mMusiclist = (ListView) view.findViewById(R.id.musiclistbyword);
        //为ListView添加数据源
        musicInfos = MediaUtil.getMusicInfos(getActivity().getApplicationContext());  //获取歌曲对象集合
        mAdapter = new MusicAdapter(getActivity(),musicInfos);
        Log.d(tag,"--------------------");
        Log.d(tag,String.valueOf(musicInfos.size()));
        mMusiclist.setAdapter(mAdapter);
        return view;
    }



}
