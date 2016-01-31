package com.sys1yagi.longeststreakandroid.model;

import com.google.gson.annotations.SerializedName;

public class Plan {

    @SerializedName("name")
    private String name;

    @SerializedName("space")
    private int space;

    @SerializedName("private_repos")
    private int privateRepos;

    @SerializedName("collaborators")
    private int collaborators;

    public String getName() {
        return name;
    }

    public int getSpace() {
        return space;
    }

    public int getPrivateRepos() {
        return privateRepos;
    }

    public int getCollaborators() {
        return collaborators;
    }
}
