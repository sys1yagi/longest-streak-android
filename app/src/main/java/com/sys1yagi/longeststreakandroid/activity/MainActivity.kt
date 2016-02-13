package com.sys1yagi.longeststreakandroid.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.sys1yagi.longeststreakandroid.LongestStreakApplication
import com.sys1yagi.longeststreakandroid.R
import com.sys1yagi.longeststreakandroid.alarm.PollingAlarmProcessor
import com.sys1yagi.longeststreakandroid.databinding.ActivityMainBinding
import com.sys1yagi.longeststreakandroid.db.Settings
import com.sys1yagi.longeststreakandroid.fragment.AccountSetupFragmentCreator
import com.sys1yagi.longeststreakandroid.fragment.MainFragmentCreator
import com.trello.rxlifecycle.components.support.RxAppCompatActivity

class MainActivity : RxAppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)
        if (savedInstanceState == null) {
            val database = (applicationContext as LongestStreakApplication).database
            if (!Settings.alreadyInitialized(database)) {
                supportFragmentManager.beginTransaction()
                        .add(R.id.content_frame, AccountSetupFragmentCreator.newBuilder().build())
                        .commit()
            } else {
                supportFragmentManager.beginTransaction()
                        .add(R.id.content_frame, MainFragmentCreator.newBuilder().build())
                        .commit()
            }
        }

        PollingAlarmProcessor.nextAlarmAfterAnHour(this)

    }
}
