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

    private class ItemDataSource extends PageKeyedDataSource<Integer, ImgListItem> {

        @Override
        public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, ImgListItem> callback) {
            Integer nextKey = null;
            try {
                nextKey = api.init();
            } catch (IOException e) {
                e.printStackTrace();
            }

            callback.onResult(api.getRet(), null, nextKey);
        }

        @Override
        public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ImgListItem> callback) {
        }

        @Override
        public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ImgListItem> callback) {
            Integer nextKey = null;
            try {
                nextKey = api.after(params.key);
            } catch (IOException e) {
                e.printStackTrace();
            }

            callback.onResult(api.getRet(), nextKey);
        }
    }

    public class ItemDataSourceFactory extends DataSource.Factory<Integer, ImgListItem> {

        @NonNull
        @Override
        public DataSource<Integer, ImgListItem> create() {
            return new ItemDataSource();
        }
    }

    interface FetchData {
        Integer init() throws IOException;

        Integer after(Integer pageNum) throws IOException;

        ArrayList<ImgListItem> getRet();
    }
}

