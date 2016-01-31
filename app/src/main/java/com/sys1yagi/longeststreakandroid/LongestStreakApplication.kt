package com.sys1yagi.longeststreakandroid

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.sys1yagi.longeststreakandroid.db.OrmaDatabase

class LongestStreakApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        database = OrmaDatabase.builder(this).build()
        AndroidThreeTen.init(this);
    }

    companion object {
        public lateinit var database: OrmaDatabase
    }
}
