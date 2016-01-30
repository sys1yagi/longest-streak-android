package com.sys1yagi.longeststreakandroid.alarm;

import com.sys1yagi.android.alarmmanagersimplify.AlarmProcessor;
import com.sys1yagi.android.alarmmanagersimplify.annotation.Simplify;

import android.content.Context;
import android.content.Intent;

@Simplify(value = "polling")
public class PollingAlarmProcessor implements AlarmProcessor {

    @Override
    public void process(Context context, Intent intent) {

    }
}
