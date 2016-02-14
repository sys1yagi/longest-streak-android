package com.sys1yagi.longeststreakandroid.tool

import android.util.Log
import com.sys1yagi.longeststreakandroid.db.OrmaDatabase
import com.sys1yagi.longeststreakandroid.model.Event
import java.util.*

class LongestStreakCounter {

    val a = Calendar.getInstance()
    val b = Calendar.getInstance()

    fun count(database: OrmaDatabase, now: Long, zoneId: String): Int {
        var streaksBegin = TimeKeeper.day(now, zoneId)
        var count = 0
        database.selectFromEventLog()
                .orderByCreatedAtDesc()
                .toList()
                .forEach {
                    System.out.println("${it.type}")
                    when (it.type) {
                        Event.Type.PUSH, Event.Type.ISSUES, Event.Type.PULL_REQUEST -> {
                            val day = TimeKeeper.day(it.createdAt, zoneId)
                            if ((streaksBegin == day && count == 0)
                                    || isMatchYesterday(streaksBegin, day)
                            ) {
                                count++
                                streaksBegin = day
                            } else if (streaksBegin != day ) {
                                return count
                            }
                        }
                    }
                }
        return count
    }

    fun isMatchYesterday(today: Long, day: Long): Boolean {
        if (today - 1 == day) {
            return true
        }

        var t = today.toInt()
        a.set(Calendar.YEAR, t / 10000)
        t = t % 10000
        a.set(Calendar.MONTH, t / 100 - 1)
        t = t % 100
        a.set(Calendar.DAY_OF_MONTH, t)

        var d = day.toInt()
        b.set(Calendar.YEAR, d / 10000)
        d = d % 10000
        b.set(Calendar.MONTH, d / 100 - 1)
        d = d % 100
        b.set(Calendar.DAY_OF_MONTH, d)

        a.add(Calendar.DAY_OF_MONTH, -1)
        return a.equals(b)
    }
}
