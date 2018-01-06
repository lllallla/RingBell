package edu.fudan.ringbell.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    private ListView mMusiclist;                   //音乐列表
    private SimpleAdapter mAdapter;
    List<MusicInfo> musicInfos = null;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflater.inflate(resource, null);

        View view = inflater.inflate(R.layout.fragment_mr_word, container, false);
        mMusiclist = (ListView) view.findViewById(R.id.musiclistbyword);
        //为ListView添加数据源
        musicInfos = MediaUtil.getMusicInfos(getActivity().getApplicationContext());  //获取歌曲对象集合
        setListAdpter(MediaUtil.getMusicMaps(musicInfos));    //显示歌曲列表
        return view;
    }


    public void setListAdpter(List<HashMap<String, String>> musiclist) {
        mAdapter = new SimpleAdapter(getActivity(), musiclist,
                R.layout.music_item, new String[]{"number","title","check_music",
                "Artist", "music_menu"}, new int[]{R.id.number,R.id.music_title,R.id.check,
                R.id.music_Artist, R.id.music_menu});
        mMusiclist.setAdapter(mAdapter);
    }

}
