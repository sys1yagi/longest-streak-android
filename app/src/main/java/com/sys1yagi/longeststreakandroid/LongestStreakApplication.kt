package com.sys1yagi.longeststreakandroid

import android.app.Application
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import com.sys1yagi.longeststreakandroid.dagger.component.AppComponent
import com.sys1yagi.longeststreakandroid.dagger.component.DaggerAppComponent
import com.sys1yagi.longeststreakandroid.db.OrmaDatabase

class LongestStreakApplication : Application() {

    companion object {
        lateinit var database: OrmaDatabase
    }

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        AndroidThreeTen.init(this);

        component = DaggerAppComponent.builder().build()
        database = OrmaDatabase.builder(this)
                .name("github_longest_streaks.db")
                .build()
    }
}
