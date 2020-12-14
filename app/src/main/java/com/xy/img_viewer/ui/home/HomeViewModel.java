package com.xy.img_viewer.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.xy.img_viewer.entity.ImgListItem;

import java.io.IOException;
import java.util.ArrayList;

public class HomeViewModel extends ViewModel {
    public LiveData<PagedList<ImgListItem>> getItemPagedList() {
        return itemPagedList;
    }

    private final LiveData<PagedList<ImgListItem>> itemPagedList;

    public HomeViewModel() {

        ItemDataSourceFactory dataSource = new ItemDataSourceFactory();
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(12).build();

        itemPagedList = (new LivePagedListBuilder<>(dataSource, pagedListConfig))
                .build();
    }

    private static class ItemDataSource extends PageKeyedDataSource<String, ImgListItem> {

        @Override
        public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<String, ImgListItem> callback) {
            try {
                Api ap = new Api();
                callback.onResult(ap.getRet(), null, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, ImgListItem> callback) {
        }

        @Override
        public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, ImgListItem> callback) {
        }
    }

    private static class ItemDataSourceFactory extends DataSource.Factory<String, ImgListItem> {

        @NonNull
        @Override
        public DataSource<String, ImgListItem> create() {
            return new ItemDataSource();
        }
    }
}