package com.sys1yagi.longeststreakandroid.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.sys1yagi.longeststreakandroid.LongestStreakApplication
import com.sys1yagi.longeststreakandroid.R
import com.sys1yagi.longeststreakandroid.alarm.PollingAlarmProcessorScheduler
import com.sys1yagi.longeststreakandroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener { view ->
            PollingAlarmProcessorScheduler.scheduleRtcWakeup(this, 1000)
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }

        if (LongestStreakApplication.database.selectFromUserName().count() > 0) {
            binding.contentMain.text.text = "It has already set up Github account.";
        } else {
            binding.contentMain.text.text = "Not yet set up Github account.";
        }
    }
}
