package com.xy.img_viewer.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.xy.img_viewer.R;
import com.xy.img_viewer.entity.ImgListItem;

public class Adapter extends PagedListAdapter<ImgListItem, Adapter.VH> {

    private final Context ctx;
    private final RequestManager glideManager;

    protected Adapter(Context ctx) {
        super(new DiffUtil.ItemCallback<ImgListItem>() {
            @Override
            public boolean areItemsTheSame(@NonNull ImgListItem oldItem, @NonNull ImgListItem newItem) {
                return oldItem.getUrl().equals(newItem.getUrl());
            }

            @Override
            public boolean areContentsTheSame(@NonNull ImgListItem oldItem, @NonNull ImgListItem newItem) {
                return oldItem.getUrl().equals(newItem.getUrl());
            }
        });
        this.ctx = ctx;
        glideManager = Glide.with(ctx);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.ctx).inflate(R.layout.img_item, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) view.getTag();
                Log.e("pos", String.valueOf(pos));
                Intent intent = new Intent();
                String a = getItem(pos).getHref();
                intent.putExtra("url", a);

                intent.setClass(ctx, DetailActivity.class);
                ctx.startActivity(intent);
            }
        });
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        ImgListItem data = getItem(position);
        holder.getTextV().setText(data.getTitle());
        holder.getItemView().setTag(position);
        glideManager.load(data.getUrl()).placeholder(new ColorDrawable(Color.GRAY)).into(holder.getImgV());
    }

    public static class VH extends RecyclerView.ViewHolder {

        public ImageView getImgV() {
            return imgV;
        }

        public void setImgV(ImageView imgV) {
            this.imgV = imgV;
        }

        public TextView getTextV() {
            return textV;
        }

        public void setTextV(TextView textV) {
            this.textV = textV;
        }

        private ImageView imgV;
        private TextView textV;

        public View getItemView() {
            return itemView;
        }

        public void setItemView(View itemView) {
            this.itemView = itemView;
        }

        private View itemView;

        public VH(@NonNull View itemView) {
            super(itemView);
            this.setItemView(itemView);
            this.setImgV(itemView.findViewById(R.id.img));
            this.setTextV(itemView.findViewById(R.id.txt));
        }
    }


    interface clickHandler {
        void onClick(String url);
    }
}