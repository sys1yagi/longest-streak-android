package com.sys1yagi.longeststreakandroid.notification

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.support.v4.app.NotificationCompat
import com.sys1yagi.longeststreakandroid.R

class LocalNotificationHelper(val context: Context) {

    fun createNotification(title: String, ticker: String, message: String): NotificationCompat.Builder {
        val notificationBuilder = NotificationCompat.Builder(context)
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
        notificationBuilder.setContentTitle(title)
        notificationBuilder.setAutoCancel(true)
        notificationBuilder.setTicker(ticker)
        notificationBuilder.setDefaults(Notification.DEFAULT_LIGHTS)
        notificationBuilder.setContentText(message)
        return notificationBuilder
    }

    fun notify(notificationId: Int, notificationBuilder: NotificationCompat.Builder?) {
        if (notificationBuilder == null) {
            return
        }
        val notification = notificationBuilder.build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notification)
    }
}
