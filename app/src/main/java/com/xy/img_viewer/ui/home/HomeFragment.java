package com.xy.img_viewer.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        Adapter adapter = new Adapter(ctx, new Adapter.EventListener() {
            @Override
            public void handler(View view, ImgListItem data) {
                Intent intent = new Intent();
                String a = data.getHref();
                intent.putExtra("url", a);
                if (ctx != null) {
                    intent.setClass(ctx, DetailActivity.class);
                    ctx.startActivity(intent);
                }
            }
        });
        recyclerView.setAdapter(adapter);


        homeViewModel.getItemPagedList().observe(getViewLifecycleOwner(), new Observer<PagedList<ImgListItem>>() {
            @Override
            public void onChanged(@Nullable PagedList<ImgListItem> item) {
                adapter.submitList(item);
            }
        });
        return root;
    }

    public static class HomeViewModel extends ViewModel {
        public LiveData<PagedList<ImgListItem>> getItemPagedList() {
            return itemPagedList;
        }

        private final LiveData<PagedList<ImgListItem>> itemPagedList;

        public HomeViewModel() {
            HomeData hData = new HomeData(new Api());
            HomeData.ItemDataSourceFactory dataSource = hData.new ItemDataSourceFactory();
            PagedList.Config pagedListConfig =
                    (new PagedList.Config.Builder())
                            .setEnablePlaceholders(false)
                            .setPrefetchDistance(1)
                            .setPageSize(1).build();

            itemPagedList = (new LivePagedListBuilder<>(dataSource, pagedListConfig))
                    .build();
        }
    }
}