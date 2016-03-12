package com.ityang.qtmusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ityang.qtmusic.R;
import com.ityang.qtmusic.bean.NetMusic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/3.
 */
public class NetMusicAdapter extends RecyclerView.Adapter<NetMusicAdapter.MyViewHolder> implements View.OnClickListener {

    private static final int TYPE_FOOT =0 ;
    private static final int TYPE_NOME = 1;
    private List<NetMusic> netMusics;
    private Context ctx;
    private List<Integer> heights;
    public NetMusicAdapter(Context ctx, List<NetMusic> netMusics) {
        this.ctx = ctx;
        this.netMusics = netMusics;
        getRandomHeight(netMusics.size());
    }

    private void getRandomHeight(int size){//得到随机item的高度
        heights = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            heights.add((int)(200+Math.random()*100));
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.list_netmusic_item, parent, false);
        //View view =View.inflate(ctx,R.layout.list_netmusic_item,null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ViewGroup.LayoutParams params =  holder.itemView.getLayoutParams();;//得到item的LayoutParams布局参数
        params.height = heights.get(position);//把随机的高度赋予item布局
        holder.itemView.setLayoutParams(params);//把params设置给item布局
        NetMusic netMusic = netMusics.get(position);
        holder.tv_title.setText(netMusic.getMusicName());
        holder.tv_singer.setText(netMusic.getArtist());
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(holder);
    }

    @Override
    public int getItemCount() {
        return netMusics.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position==netMusics.size()-1){
            return TYPE_FOOT;
        }else {
            return TYPE_NOME;
        }

    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(v,((MyViewHolder) v.getTag()).getAdapterPosition());
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_singer;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_singer = (TextView) itemView.findViewById(R.id.tv_singer);
        }
    }
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    /**
     * 回调接口
     */
    public interface OnItemClickListener {
        void onItemClick(View v, int position);

    }
}
