package com.xy.img_viewer.ui.home;

import androidx.annotation.NonNull;
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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xy.img_viewer.R;
import com.xy.img_viewer.entity.ImgListItem;

import java.lang.reflect.InvocationTargetException;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        HomeViewModel homeViewModel = new ViewModelProvider(this, new HomeViewModelFactory(url)).get(HomeViewModel.class);

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
    }

    public static class HomeViewModelFactory implements ViewModelProvider.Factory {
        public String url;

        public HomeViewModelFactory(String url) {
            this.url = url;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            try {
                return modelClass.getConstructor(String.class).newInstance(url);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            //noinspection ConstantConditions
            return null;
        }
    }

    public static class HomeViewModel extends ViewModel {
        public LiveData<PagedList<ImgListItem>> getItemPagedList() {
            return itemPagedList;
        }

        private final LiveData<PagedList<ImgListItem>> itemPagedList;

        public HomeViewModel(String url) {
            DetailApi api = new DetailApi();
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