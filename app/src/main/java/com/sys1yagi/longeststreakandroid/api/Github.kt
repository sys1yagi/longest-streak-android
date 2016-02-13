package com.sys1yagi.longeststreakandroid.api


import com.sys1yagi.longeststreakandroid.model.Event

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

interface Github {
    @GET("users/{user}/events/public")
    fun userEvents(@Path("user") userName: String, @Query("page")page:Int = 1): Observable<Response<List<Event>>>
}
