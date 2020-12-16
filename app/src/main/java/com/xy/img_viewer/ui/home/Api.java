package com.xy.img_viewer.ui.home;

import android.util.Log;

import com.xy.img_viewer.entity.ImgListItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Api implements HomeData.FetchData {

    public ArrayList<ImgListItem> getRet() {
        return ret;
    }

    private ArrayList<ImgListItem> ret = new ArrayList<>();


    private void request(Integer pageNum) throws IOException {
        String url = String.format("https://www.bkj233b.top/page/%s/", pageNum);

        ArrayList<ImgListItem> ret1 = new ArrayList<>();

        Document doc = Jsoup.connect(url).get();
        Element parent = doc.getElementById("masonry");
        Elements img = parent.select(".item-img");
        Elements a = parent.select(".item-link");

        for (int i = 0; i < img.size(); i++) {
            ret1.add(new ImgListItem(img.get(i).attr("data-original"), img.get(i).attr("alt"), a.get(i).attr("href")));
        }
        ret = ret1;
    }

    public Integer init() throws IOException {
        request(1);
        return 2;
    }

    public Integer after(Integer pageNum) throws IOException {
        request(pageNum);
        return pageNum + 1 > 17 ? null : pageNum + 1;
    }

    public Api() {
    }
}
