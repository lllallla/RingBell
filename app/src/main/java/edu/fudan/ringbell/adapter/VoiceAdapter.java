package edu.fudan.ringbell.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import edu.fudan.ringbell.MakeRIngActivity;
import edu.fudan.ringbell.R;
import edu.fudan.ringbell.entity.VoiceInfo;


/**
 * Created by niuzhenghao on 2018/1/7.
 */

public class VoiceAdapter extends BaseAdapter {
    private String tag = "VoiceAdapter";
    private Context context;
    private List<VoiceInfo> voiceList;

    public VoiceAdapter(Context context, List<VoiceInfo> list) {
        this.context = context;
        this.voiceList = list;
    }

    @Override
    public int getCount() {
        return voiceList.size();
    }

    @Override
    public Object getItem(int position) {
        return voiceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        VoiceListener VoiceListener=null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(this.context).inflate(R.layout.voice_item, null);
            holder.number = (TextView) convertView.findViewById(R.id.number);
            holder.title = (TextView) convertView.findViewById(R.id.voice_title);
            holder.menu = (ImageView) convertView.findViewById(R.id.music_menu);
            holder.jump = (LinearLayout) convertView.findViewById(R.id.voicejump);
            int num = position+1;
            holder.number.setText(num+"");
            holder.title.setText(this.voiceList.get(position).getTitle());
            holder.menu.setImageResource(R.drawable.music_menu);
            holder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context).setTitle("确认").setMessage("菜单监视")
                            .setPositiveButton("是",null)
                            .setNegativeButton("否",null).show();
                }
            });
            VoiceListener=new VoiceListener(position);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            int num = position+1;
            holder.number.setText(num+"");
            holder.title.setText(this.voiceList.get(position).getTitle());
            holder.menu.setImageResource(R.drawable.music_menu);
        }
        holder.jump.setOnClickListener(VoiceListener);
        return  convertView;
    }
    private class VoiceListener implements View.OnClickListener {
        int mPosition;
        public VoiceListener(int inPosition){
            mPosition= inPosition;
        }
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent intent=new Intent(context, MakeRIngActivity.class);
            context.startActivity(intent);
        }
    }


    static class ViewHolder {
        LinearLayout jump;
        TextView number;
        ImageView menu;
        TextView title;
    }
}
