package com.sys1yagi.longeststreakandroid

import android.app.Application
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import com.sys1yagi.longeststreakandroid.db.OrmaDatabase

class LongestStreakApplication : Application() {

    companion object {
        lateinit var database: OrmaDatabase
    }

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        AndroidThreeTen.init(this);

        database = OrmaDatabase.builder(this)
                .name("github_longest_streaks.db")
                .build()
        //        if (!Settings.alreadyInitialized(database)) {
        //            val settings = Settings()
        //            settings.name = "sys1yagi"
        //            settings.email = "sylc.yagi@gmai.com"
        //            settings.zoneId = "Asia/Tokyo"
        //            database.insertIntoSettings(settings)
        //        }
    }
}
