package edu.fudan.ringbell.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.fudan.ringbell.R;
import edu.fudan.ringbell.entity.MusicInfo;

/**
 * Created by niuzhenghao on 2018/1/7.
 */

public class MusicAdapter extends BaseAdapter {
    private Context context;
    private List<MusicInfo> musciList = new ArrayList<>();

    public MusicAdapter(Context context, List<MusicInfo> list) {
        this.context = context;
        musciList = list;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(this.context).inflate(R.layout.music_item, null);
            holder.number = (TextView) convertView.findViewById(R.id.number);
            holder.artistI = (ImageView) convertView.findViewById(R.id.check);
            holder.title = (TextView) convertView.findViewById(R.id.music_title);
            holder.menu = (ImageView) convertView.findViewById(R.id.music_menu);
            holder.artistT = (TextView) convertView.findViewById(R.id.music_Artist);
            holder.number.setText(position+"");
            holder.title.setText(musciList.get(position).getTitle());
            holder.artistT.setText(musciList.get(position).getArtist());
            holder.artistI.setImageResource(R.drawable.lay_icn_cartist);
            holder.menu.setImageResource(R.drawable.music_menu);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.number.setText(position+"");
            holder.title.setText(musciList.get(position).getTitle());
            holder.artistT.setText(musciList.get(position).getArtist());
            holder.artistI.setImageResource(R.drawable.lay_icn_cartist);
            holder.menu.setImageResource(R.drawable.music_menu);
        }
        return  convertView;
    }

    static class ViewHolder {
        TextView number;
        ImageView artistI;
        ImageView menu;
        TextView title;
        TextView artistT;
    }
}
