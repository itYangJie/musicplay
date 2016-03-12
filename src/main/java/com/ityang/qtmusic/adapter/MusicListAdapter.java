package com.ityang.qtmusic.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ityang.qtmusic.R;
import com.ityang.qtmusic.bean.MusicInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/2/25.
 */
public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MyViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private List<MusicInfo> musicInfos;
    private Context ctx;
    private boolean isFlagCurrent;

    public MusicListAdapter(Context ctx, List<MusicInfo> musicInfos, boolean isFlagCurrent) {
        this.ctx = ctx;
        this.musicInfos = musicInfos;
        this.isFlagCurrent = isFlagCurrent;
    }


    private int currentPosition = 0;

    public void myNotifyDataSetChanged(int position) {
        currentPosition = position;
        notifyDataSetChanged();
    }


    @Override
    public MusicListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(ctx, R.layout.list_mymusic_item, null);
        MusicListAdapter.MyViewHolder myViewHolder = new MusicListAdapter.MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MusicListAdapter.MyViewHolder holder, int position) {
        MusicInfo musicInfo = musicInfos.get(position);
        holder.tv_title.setText(musicInfo.getTitle());
        holder.tv_singer.setText(musicInfo.getArtist());
        holder.tv_time.setText(musicInfo.getTime());
        if (isFlagCurrent) {
            if (position == currentPosition) {
                holder.tv_title.setTextColor(Color.BLUE);
                holder.tv_singer.setTextColor(Color.BLUE);
                holder.tv_time.setTextColor(Color.BLUE);
                holder.imageView.setImageResource(R.drawable.app_logo2);

            } else {
                holder.tv_title.setTextColor(Color.GRAY);
                holder.tv_singer.setTextColor(Color.GRAY);
                holder.tv_time.setTextColor(Color.GRAY);
                holder.imageView.setImageResource(R.drawable.music);
            }
        }
        holder.itemView.setOnClickListener(this);
        holder.itemView.setOnLongClickListener(this);
        holder.itemView.setTag(holder);
    }

    @Override
    public int getItemCount() {
        return musicInfos.size();
    }

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }



    /**
     * 回调接口
     */
    public interface OnItemClickListener {
        void onItemClick(View v, int position);

    }
    public interface OnItemLongClickListener {
        void onItemLongClick(View v, int position);

    }
    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(v,((MusicListAdapter.MyViewHolder) v.getTag()).getAdapterPosition());
        }
    }
    @Override
    public boolean onLongClick(View v) {
        if (onItemLongClickListener != null) {
            onItemLongClickListener.onItemLongClick(v, ((MusicListAdapter.MyViewHolder) v.getTag()).getAdapterPosition());
        }
        return false;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_singer, tv_time;
        ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_singer = (TextView) itemView.findViewById(R.id.tv_singer);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            imageView = (ImageView) itemView.findViewById(R.id.imageview_icon);
        }
    }
}
