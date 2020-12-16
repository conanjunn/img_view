package com.xy.img_viewer.ui.home;

import com.xy.img_viewer.entity.ImgListItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class DetailApi implements HomeData.FetchData {
    private final ArrayList<ImgListItem> ret = new ArrayList<>();

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

    private void request() throws IOException {
        Document doc = Jsoup.connect(url).get();
        Element parent = doc.getElementById("masonry");
        Elements img = parent.select(".post-item-img");

        for (int i = 0; i < img.size(); i++) {
            ret.add(new ImgListItem(img.get(i).attr("data-original"), img.get(i).attr("alt"), ""));
        }
    }

    @Override
    public Integer init() throws IOException {
        request();
        return null;
    }

    @Override
    public Integer after(Integer pageNum) {
        return null;
    }

    @Override
    public ArrayList<ImgListItem> getRet() {
        return ret;
    }
}
