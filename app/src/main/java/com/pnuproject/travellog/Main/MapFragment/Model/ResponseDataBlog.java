package com.pnuproject.travellog.Main.MapFragment.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.ArrayList;

public class ResponseDataBlog {
    @SerializedName("documents")
    private ArrayList<BlogDocuments> documents;

    public ArrayList<BlogDocuments> getDocuments() {
        return documents;
    }

    public class BlogDocuments {
        @SerializedName("blogname")
        String blogname;

        @SerializedName("contents")
        String contents;

        @SerializedName("datetime")
        String datetime;

        @SerializedName("thumbnail")
        String thumbnail_url;

        @SerializedName("title")
        String title;

        @SerializedName("url")
        String url;


        public String getBlogname() {
            return blogname;
        }

        public String getContents() {
            return contents;
        }

        public String getDatetime() {
            return datetime;
        }

        public String getThumbnail_url() {
            return thumbnail_url;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }

        public void setContents(String contents) {
            this.contents = contents;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
