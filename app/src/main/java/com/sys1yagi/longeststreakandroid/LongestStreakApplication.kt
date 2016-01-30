package com.sys1yagi.longeststreakandroid

import android.app.Application
import com.sys1yagi.longeststreakandroid.model.OrmaDatabase

class LongestStreakApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        database = OrmaDatabase.builder(this).build()
    }

    companion object {
        public lateinit var database: OrmaDatabase
    }
}
