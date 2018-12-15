package com.example.lenovo_pc.zhihuribao.Activity.Activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.lenovo_pc.zhihuribao.R;

import java.util.List;

class HotAdapter extends RecyclerView.Adapter<HotAdapter.ViewHolder> {

    private static OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public static void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }



    private List<Hot> list;

    static class ViewHolder extends RecyclerView.ViewHolder {
        //绑定点击事件
        WebView hotImage;
        TextView hotTitle;
        View hotView;             //这个view指的是自身（这一整个item）

        public ViewHolder(View view) {
            super(view);
            hotView = view;
            hotImage = (WebView) view.findViewById(R.id.iv_picture_hot);
            hotTitle = (TextView) view.findViewById(R.id.tv_title_hot);
        }
    }

    public HotAdapter(List<Hot> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public HotAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_hot, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.hotImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Hot hot = list.get(position);
                mOnItemClickListener.onItemClick(v, position);
            }
        });


        holder.hotTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Hot hot = list.get(position);
                mOnItemClickListener.onItemClick(v, position);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Hot hot = list.get(i);

        viewHolder.hotImage.getSettings().setJavaScriptEnabled(true);
        viewHolder.hotImage.setWebViewClient(new WebViewClient());
        viewHolder.hotImage.loadUrl(String.valueOf(hot.getImageId()));
        viewHolder.hotTitle.setText(String.valueOf(hot.getTitle()));
    }




    @Override
    public int getItemCount() {
        return list.size();
    }

}

