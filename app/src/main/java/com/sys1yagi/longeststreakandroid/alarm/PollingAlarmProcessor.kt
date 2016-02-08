package com.sys1yagi.longeststreakandroid.alarm

import android.content.Context
import android.content.Intent
import android.util.Log
import com.sys1yagi.android.alarmmanagersimplify.AlarmProcessor
import com.sys1yagi.android.alarmmanagersimplify.annotation.Simplify
import com.sys1yagi.longeststreakandroid.LongestStreakApplication
import com.sys1yagi.longeststreakandroid.api.GithubService
import com.sys1yagi.longeststreakandroid.db.Settings
import com.sys1yagi.longeststreakandroid.notification.LocalNotificationHelper
import com.sys1yagi.longeststreakandroid.tool.PublicContributionJudgement
import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import java.util.concurrent.TimeUnit

@Simplify(value = "polling")
class PollingAlarmProcessor : AlarmProcessor {
    companion object {
        val TAG = "PollingAlarmProcessor"
        val JUDGEMENT = PublicContributionJudgement()
        fun nextAlarmAfterFiveMinutes(context: Context) {
            Log.d(TAG, "Reserve 5 minutes")
            PollingAlarmProcessorScheduler.scheduleRtcWakeup(context, TimeUnit.MINUTES.toMillis(5).toInt())
        }

        fun nextAlarmAfterAnHour(context: Context) {
            Log.d(TAG, "Reserve an hour")
            PollingAlarmProcessorScheduler.scheduleRtcWakeup(context, TimeUnit.HOURS.toMillis(1L).toInt())
        }

        fun nextAlarmTomorrow(context: Context, now: Long, zoneId: String) {
            Log.d(TAG, "Reserve tomorrow")
            val zone = ZoneId.of(zoneId);
            val today = OffsetDateTime.ofInstant(Instant.ofEpochMilli(now), zone)
            val tomorrow = today.plusDays(1)

            val tomorrowDate = ZonedDateTime.of(tomorrow.year, tomorrow.monthValue, tomorrow.dayOfMonth, 0, 0, 0, 0, zone)

            val interval = tomorrowDate.toInstant().toEpochMilli() - today.toInstant().toEpochMilli()
            PollingAlarmProcessorScheduler.scheduleRtcWakeup(context, interval.toInt())
        }
    }

    override fun process(context: Context, intent: Intent) {
        Log.d(TAG, "PollingAlarmProcessor.process")
        if (!Settings.alreadyInitialized(LongestStreakApplication.database)) {
            Log.d(TAG, "Account not initialized yet")
            nextAlarmAfterAnHour(context)
            return
        }
        val settings = Settings.getRecord(LongestStreakApplication.database)
        GithubService.client.userEvents(settings.name)
                .subscribe(
                        { events ->
                            val now = System.currentTimeMillis()
                            val count = JUDGEMENT.todayContributionCount(settings.name,
                                    settings.zoneId,
                                    now,
                                    events)
                            if ( count == 0) {
                                Log.d(TAG, "Not yet! The next alarm is an hour later.")
                                notifyNotYetContribution(context)
                                nextAlarmAfterAnHour(context)
                            } else {
                                Log.d(TAG, "Already you contributed today! Next alarm is tomorrow.")
                                notifyAlreadyContributedToday(context)
                                nextAlarmTomorrow(context, now, settings.zoneId)
                            }
                        },
                        { error ->
                            //no op
                            Log.d(TAG, "Error. retry 5 minutes later.")
                            nextAlarmAfterFiveMinutes(context)
                        }
                )
    }

    fun notifyAlreadyContributedToday(context: Context) {
        val helper = LocalNotificationHelper(context)
        val title = "Yes! Already Contributed Today...!"

        helper.notify(10, helper.createNotification(title, title, "Github longest streak"))
    }

    fun notifyNotYetContribution(context: Context) {
        val helper = LocalNotificationHelper(context)
        val title = "Not Yet Contribution Today...!"

        helper.notify(10, helper.createNotification(title, title, "Github longest streak"))
    }
}
