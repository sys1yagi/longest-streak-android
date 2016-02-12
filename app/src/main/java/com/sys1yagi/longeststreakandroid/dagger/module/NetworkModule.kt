package com.sys1yagi.longeststreakandroid.dagger.module

import com.sys1yagi.longeststreakandroid.api.Github
import com.sys1yagi.longeststreakandroid.tool.GsonProvider
import dagger.Module
import dagger.Provides
import retrofit2.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.RxJavaCallAdapterFactory
import javax.inject.Singleton

@Singleton
@Module
class NetworkModule {

    @Provides
    fun provideGithub(): Github {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonProvider.instance))
                .build()
        return retrofit.create(Github::class.java)
    }
}
