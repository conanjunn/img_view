package com.xy.img_viewer.ui.home;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.xy.img_viewer.entity.ImgListItem;

import java.io.IOException;
import java.util.ArrayList;

public class HomeData {

    private final FetchData api;

    public HomeData(FetchData api) {
        this.api = api;
    }

    private class ItemDataSource extends PageKeyedDataSource<String, ImgListItem> {

        @Override
        public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<String, ImgListItem> callback) {
            try {
                api.request();
            } catch (IOException e) {
                e.printStackTrace();
            }
            callback.onResult(api.getRet(), null, null);
        }

        @Override
        public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, ImgListItem> callback) {
        }

        @Override
        public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, ImgListItem> callback) {
        }
    }

    public class ItemDataSourceFactory extends DataSource.Factory<String, ImgListItem> {

        @NonNull
        @Override
        public DataSource<String, ImgListItem> create() {
            return new ItemDataSource();
        }
    }

    interface FetchData {
        void request() throws IOException;

        ArrayList<ImgListItem> getRet();
    }
}

