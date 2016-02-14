package com.sys1yagi.longeststreakandroid.dagger.module

import android.content.Context
import com.sys1yagi.longeststreakandroid.db.OrmaDatabase
import com.sys1yagi.longeststreakandroid.tool.LongestStreakCounter
import javax.inject.Singleton

import dagger.Module
import dagger.Provides

@Singleton
@Module
class IoModule(val context: Context) {

    @Provides
    fun provideLongestStreakCounter(): LongestStreakCounter {
        return LongestStreakCounter()
    }
}
