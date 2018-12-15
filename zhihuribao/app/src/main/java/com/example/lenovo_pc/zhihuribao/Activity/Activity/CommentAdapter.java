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

class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private static OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public static void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }



    private List<Comment> list;

    static class ViewHolder extends RecyclerView.ViewHolder {
        //绑定点击事件
        WebView authorImage;
        TextView author;
        TextView content;
        TextView good;
        TextView time;
        View commentView;             //这个view指的是自身（这一整个item）

        public ViewHolder(View view) {
            super(view);
            commentView = view;
            authorImage = (WebView) view.findViewById(R.id.wv_comment);
            author = (TextView) view.findViewById(R.id.tv_author);
            content = (TextView) view.findViewById(R.id.tv_content);
            good = (TextView) view.findViewById(R.id.tv_good);
            time = (TextView) view.findViewById(R.id.tv_time);
        }
    }

    public CommentAdapter(List<Comment> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_comment, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
//        holder.authorImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                Comment comment = list.get(position);
//                mOnItemClickListener.onItemClick(v, position);
//            }
//        });
//
//
//        holder.hotTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                Hot hot = list.get(position);
//                mOnItemClickListener.onItemClick(v, position);
//            }
//        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Comment comment = list.get(i);
        viewHolder.authorImage.getSettings().setJavaScriptEnabled(true);
        viewHolder.authorImage.setWebViewClient(new WebViewClient());
        viewHolder.authorImage.loadUrl(String.valueOf(comment.getimageId()));
        viewHolder.author.setText(String.valueOf(comment.getAuthor()));
        viewHolder.good.setText(String.valueOf(comment.getGood()));
        viewHolder.content.setText(String.valueOf(comment.getContent()));
        viewHolder.time.setText(String.valueOf(comment.getTime()));
    }




    @Override
    public int getItemCount() {
        return list.size();
    }

}