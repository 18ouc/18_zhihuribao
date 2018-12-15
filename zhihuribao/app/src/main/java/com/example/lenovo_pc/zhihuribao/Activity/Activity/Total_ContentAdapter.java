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

import static java.lang.Integer.parseInt;

class Total_ContentAdapter extends RecyclerView.Adapter<Total_ContentAdapter.ViewHolder> {
    private static OnItemClickListener mOnItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public static void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }



    private List<Total_Content> list;

    static class ViewHolder extends RecyclerView.ViewHolder {



        //绑定点击事件
        WebView total_contentImage;
        TextView total_contentName;
        TextView total_contentDate;
        View total_contentView;             //这个view指的是自身（这一整个item）

        public ViewHolder(View view) {
            super(view);
            total_contentView = view;
            total_contentDate = (TextView)view.findViewById(R.id.tv_date) ;
            total_contentName = (TextView)view.findViewById(R.id.tv_content) ;
            total_contentImage = (WebView) view.findViewById(R.id.iv_picture);
            //   totalDescription = (TextView) view.findViewById(R.id.tv_description);
        }
    }

    public Total_ContentAdapter(List<Total_Content> list) {
        this.list = list;
    }
    @NonNull
    @Override
    public Total_ContentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_total_content, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.total_contentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Total_Content total_content = list.get(position);
                mOnItemClickListener.onItemClick(v, position);
            }
        });

       holder.total_contentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                mOnItemClickListener.onItemClick(v, position);
            }
        });


       holder.total_contentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                mOnItemClickListener.onItemClick(v, position);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Total_Content total_content = list.get(i);

        viewHolder.total_contentImage.getSettings().setJavaScriptEnabled(true);
        viewHolder.total_contentImage.setWebViewClient(new WebViewClient());
        viewHolder.total_contentImage.loadUrl(total_content.getimageId());
        viewHolder.total_contentName.setText(total_content.getName());
        viewHolder.total_contentDate.setText(total_content.getDate());

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

}

