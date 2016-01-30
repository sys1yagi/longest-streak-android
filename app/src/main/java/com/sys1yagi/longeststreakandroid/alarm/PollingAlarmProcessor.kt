package com.sys1yagi.longeststreakandroid.alarm

import android.content.Context
import android.content.Intent
import com.sys1yagi.android.alarmmanagersimplify.AlarmProcessor
import com.sys1yagi.android.alarmmanagersimplify.annotation.Simplify

@Simplify(value = "polling")
class PollingAlarmProcessor : AlarmProcessor {

    override fun process(context: Context, intent: Intent) {

    }
}
