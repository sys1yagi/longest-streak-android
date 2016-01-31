package com.sys1yagi.longeststreakandroid.model;

import com.google.gson.annotations.SerializedName;

public class Commit {

    @SerializedName("sha")
    private String sha;

    @SerializedName("author")
    private Author author;

    @SerializedName("message")
    private String message;

    @SerializedName("distinct")
    private boolean distinct;

    @SerializedName("url")
    private String url;

    public String getSha() {
        return sha;
    }

    public Author getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public String getUrl() {
        return url;
    }
}
