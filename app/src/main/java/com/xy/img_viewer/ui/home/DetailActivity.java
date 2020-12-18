package com.xy.img_viewer.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

import java.io.File;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.init(url);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setHasFixedSize(true);
        Adapter adapter = new Adapter(this, new Adapter.EventListener() {
            @Override
            public void handler(View view, ImgListItem itemData) {
            }
        });
        recyclerView.setAdapter(adapter);

        homeViewModel.getItemPagedList().observe(this, new Observer<PagedList<ImgListItem>>() {
            @Override
            public void onChanged(@Nullable PagedList<ImgListItem> item) {
                adapter.submitList(item);
            }
        });

        Context that = this;

        findViewById(R.id.download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ImgListItem> list = homeViewModel.api.getRet();
                if (list.size() == 0) {
                    Toast.makeText(that, "数据未加载完毕", Toast.LENGTH_LONG).show();
                    return;
                }
                File directory = new File(that.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath(), list.get(0).getTitle());
                if (!directory.exists()) {
                    boolean isSucc = directory.mkdir();
                    if (!isSucc) {
                        Toast.makeText(that, "文件夹创建失败", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                DownloadImg.Params[] myList = new DownloadImg.Params[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    myList[i] = new DownloadImg.Params(list.get(i).getUrl(), directory);
                }
                new DownloadImg(that).execute(myList);
            }
        });
    }

    public static class HomeViewModel extends ViewModel {
        public LiveData<PagedList<ImgListItem>> getItemPagedList() {
            return itemPagedList;
        }

        private LiveData<PagedList<ImgListItem>> itemPagedList;

        public DetailApi api;

        public void init(String url) {
            api = new DetailApi();
            api.setUrl(url);
            HomeData hData = new HomeData(api);
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