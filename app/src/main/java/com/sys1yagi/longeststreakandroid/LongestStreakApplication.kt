package com.sys1yagi.longeststreakandroid

import android.app.Application
import com.chibatching.kotpref.Kotpref
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen

class LongestStreakApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Kotpref.init(this)
        Stetho.initializeWithDefaults(this)
        AndroidThreeTen.init(this);
    }
}
