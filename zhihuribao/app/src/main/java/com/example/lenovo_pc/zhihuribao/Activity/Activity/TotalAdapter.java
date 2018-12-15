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

class TotalAdapter extends RecyclerView.Adapter<TotalAdapter.ViewHolder> {
//    public static Object notifyDataSetChanged;
    private static OnItemClickListener mOnItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public static void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }



    private List<Total> list;

    static class ViewHolder extends RecyclerView.ViewHolder {



        //绑定点击事件
        WebView totalImage;
        TextView totalDescription;
        TextView totalName;
        View totalView;             //这个view指的是自身（这一整个item）

        public ViewHolder(View view) {
            super(view);
            totalView = view;
            totalName = (TextView)view.findViewById(R.id.tv_name) ;
            totalImage = (WebView) view.findViewById(R.id.iv_picture);
         //   totalDescription = (TextView) view.findViewById(R.id.tv_description);
        }
    }

    public TotalAdapter(List<Total> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public TotalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_total, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.totalName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                mOnItemClickListener.onItemClick(v, position);
            }
        });

       /* holder.totalImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Total total = list.get(position);
                Toast.makeText(view.getContext(), "Sorry,本APP暂时没有设置修改图片功能~", Toast.LENGTH_SHORT).show();
            }
        });*/


       /* holder.totalName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Total total = list.get(position);
                //Toast.makeText(view.getContext(), shop.getLocation(),Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(view.getContext(), Main7Activity.class);
                // intent.putExtra("shop_Name",shop.getName());
                //intent.putExtra("shop_Name37",shop.getName());
                //Toast.makeText(view.getContext(), shop.getName(),Toast.LENGTH_SHORT).show();
                mOnItemClickListener.onItemClick(v, position);
            }
        });*/

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Total total = list.get(i);

        viewHolder.totalImage.getSettings().setJavaScriptEnabled(true);
        viewHolder.totalImage.setWebViewClient(new WebViewClient());
        viewHolder.totalImage.loadUrl(String.valueOf(total.getimageId()));
        viewHolder.totalName.setText(String.valueOf(total.getName()));
      //  viewHolder.totalDescription.setText(String.valueOf(total.getDescription()));

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

}

