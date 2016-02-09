package com.sys1yagi.longeststreakandroid.api

import com.sys1yagi.longeststreakandroid.tool.GsonProvider
import retrofit2.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.RxJavaCallAdapterFactory

class GithubService {
    companion object {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonProvider.instance))
                .build()

        val client = retrofit.create(Github::class.java)
    }
}
