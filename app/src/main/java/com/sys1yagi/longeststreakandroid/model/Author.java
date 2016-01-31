package com.sys1yagi.longeststreakandroid.model;

import com.google.gson.annotations.SerializedName;

public class Author {

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
