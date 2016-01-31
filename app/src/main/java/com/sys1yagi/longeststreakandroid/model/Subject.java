package com.sys1yagi.longeststreakandroid.model;

import com.google.gson.annotations.SerializedName;

public class Subject {

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    @SerializedName("latest_comment_url")
    private String latestCommentUrl;

    @SerializedName("type")
    private String type;

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getLatestCommentUrl() {
        return latestCommentUrl;
    }

    public String getType() {
        return type;
    }
}
