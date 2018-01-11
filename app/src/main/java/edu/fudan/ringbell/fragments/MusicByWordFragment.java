package edu.fudan.ringbell.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.Collections;
import java.util.Comparator;
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
    private ListView mMusiclist;                   //音乐列表
    private MusicAdapter mAdapter;
    List<MusicInfo> musicInfos = null;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflater.inflate(resource, null);

        View view = inflater.inflate(R.layout.fragment_mr_word, container, false);
        mMusiclist = (ListView) view.findViewById(R.id.musiclistbyword);
        //为ListView添加数据源
        musicInfos = MediaUtil.getMusicInfos(getActivity().getApplicationContext());  //获取歌曲对象集合
        Comparator c = new Comparator<MusicInfo>() {
            @Override
            public int compare(MusicInfo m1, MusicInfo m2) {
                // TODO Auto-generated method stub
                    return m1.getTitle().compareTo(m2.getTitle());
            }
        };
        Collections.sort(musicInfos,c);
        mAdapter =  new MusicAdapter(this.getActivity(),musicInfos);
        mMusiclist.setAdapter(mAdapter);
        return view;
    }


}
