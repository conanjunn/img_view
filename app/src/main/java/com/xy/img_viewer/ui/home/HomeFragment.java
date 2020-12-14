package com.xy.img_viewer.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.xy.img_viewer.R;
import com.xy.img_viewer.entity.ImgListItem;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        Context ctx = getContext();
        RecyclerView recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
//        recyclerView.setHasFixedSize(true);
        HomeAdapter adapter = new HomeAdapter(ctx);
        recyclerView.setAdapter(adapter);


        homeViewModel.getItemPagedList().observe(getViewLifecycleOwner(), new Observer<PagedList<ImgListItem>>() {
            @Override
            public void onChanged(@Nullable PagedList<ImgListItem> item) {
                adapter.submitList(item);
            }
        });
        return root;
    }

    private static class HomeAdapter extends PagedListAdapter<ImgListItem, VH> {

        private final Context ctx;
        private final RequestManager glideManager;

        protected HomeAdapter(Context ctx) {
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
    }

    private static class VH extends RecyclerView.ViewHolder {

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
}