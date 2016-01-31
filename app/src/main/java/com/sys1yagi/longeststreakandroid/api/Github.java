package com.sys1yagi.longeststreakandroid.api;


import com.sys1yagi.longeststreakandroid.model.Event;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface Github {

    @GET("users/{user}/events/public")
    Observable<List<Event>> userEvents(@Path("user") String userName);
}
