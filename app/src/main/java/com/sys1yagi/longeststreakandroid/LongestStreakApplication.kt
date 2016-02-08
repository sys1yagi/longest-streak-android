package com.sys1yagi.longeststreakandroid

import android.app.Application
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import com.sys1yagi.longeststreakandroid.db.OrmaDatabase
import com.sys1yagi.longeststreakandroid.db.Settings

class LongestStreakApplication : Application() {

    companion object {
        lateinit var database: OrmaDatabase
    }

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        AndroidThreeTen.init(this);

        database = OrmaDatabase.builder(this).build()
        if (Settings.getRecord(database) == null) {
            val settings = Settings()
            settings.name = "sys1yagi"
            settings.email = "sylc.yagi@gmai.com"
            settings.zoneId = "Asia/Tokyo"
            database.insertIntoSettings(settings)
        }
    }
}
