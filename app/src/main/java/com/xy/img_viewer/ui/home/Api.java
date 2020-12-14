package com.xy.img_viewer.ui.home;

import com.xy.img_viewer.entity.ImgListItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Api {
    public ArrayList<ImgListItem> getRet() {
        return ret;
    }

    private final ArrayList<ImgListItem> ret = new ArrayList<>();

    public Api() throws IOException {
        Document doc = Jsoup.connect("https://www.bkj233b.top/?iao.su").get();
        Element parent = doc.getElementById("masonry");
        Elements img = parent.select(".item-img");
        Elements a = parent.select(".item-link");

        for (int i = 0; i < img.size(); i++) {
            ret.add(new ImgListItem(img.get(i).attr("data-original"), img.get(i).attr("alt"), a.get(i).attr("href")));
        }
    }
}
