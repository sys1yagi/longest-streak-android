package com.sys1yagi.longeststreakandroid.tool

import com.sys1yagi.longeststreakandroid.model.Event
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

object TimeKeeper{
    fun isMatchDate(zoneId: String, now: Long, event: Event): Boolean {
        return day(now, zoneId) == day(event.createdAt.time, zoneId)
    }

    fun day(time: Long, zoneId: String): Long {
        val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.of(zoneId))
        return dateTime.year * 10000L + dateTime.monthValue * 100L + dateTime.dayOfMonth
    }
}
