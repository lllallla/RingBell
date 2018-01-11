package edu.fudan.ringbell.adapter;

import android.app.AlertDialog;
import android.content.Context;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import edu.fudan.ringbell.PlayMusicActivity;
import edu.fudan.ringbell.R;
import edu.fudan.ringbell.entity.MusicInfo;


/**
 * Created by niuzhenghao on 2018/1/7.
 */

public class MusicAdapter extends BaseAdapter {
    private String tag = "musicAdapter";
    private Context context;
    private List<MusicInfo> musciList;

    public MusicAdapter(Context context, List<MusicInfo> list) {
        this.context = context;
        this.musciList = list;
    }

    @Override
    public int getCount() {
        return musciList.size();
    }

    @Override
    public Object getItem(int position) {
        return musciList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        MusicListener MusicListener=null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(this.context).inflate(R.layout.music_item, null);
            holder.number = (TextView) convertView.findViewById(R.id.number);
            holder.artistI = (ImageView) convertView.findViewById(R.id.check);
            holder.title = (TextView) convertView.findViewById(R.id.music_title);
            holder.menu = (ImageView) convertView.findViewById(R.id.music_menu);
            holder.artistT = (TextView) convertView.findViewById(R.id.music_Artist);
            holder.jump = (LinearLayout) convertView.findViewById(R.id.musicjump);
            int num = position+1;
            holder.number.setText(num+"");
            holder.title.setText(this.musciList.get(position).getTitle());
            holder.artistT.setText(this.musciList.get(position).getArtist());
            holder.artistI.setImageResource(R.drawable.lay_icn_cartist);
            holder.menu.setImageResource(R.drawable.music_menu);
            holder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context).setTitle("确认").setMessage("是否上传本铃声至云端？")
                            .setPositiveButton("是",null)
                            .setNegativeButton("否",null).show();
                }
            });
            MusicListener=new MusicListener(position);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            int num = position+1;
            holder.number.setText(num+"");
            holder.title.setText(this.musciList.get(position).getTitle());
            holder.artistT.setText(this.musciList.get(position).getArtist());
            holder.artistI.setImageResource(R.drawable.lay_icn_cartist);
            holder.menu.setImageResource(R.drawable.music_menu);
        }
        holder.jump.setOnClickListener(MusicListener);
        return  convertView;
    }
    private class MusicListener implements View.OnClickListener {
        int mPosition;
        public MusicListener(int inPosition){
            mPosition= inPosition;
        }
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent intent=new Intent(context, PlayMusicActivity.class);
            context.startActivity(intent);
        }
    }


    static class ViewHolder {
        LinearLayout jump;
        TextView number;
        ImageView artistI;
        ImageView menu;
        TextView title;
        TextView artistT;
    }
}
