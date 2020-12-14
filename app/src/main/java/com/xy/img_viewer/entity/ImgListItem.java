package com.xy.img_viewer.entity;

public class ImgListItem {
    private String url;
    private String title;

    public String getHref() {
        return Href;
    }

    public void setHref(String href) {
        Href = href;
    }

    private String Href;

    public ImgListItem(String url, String title, String href) {
        this.url = url;
        this.title = title;
        this.setHref(href);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
