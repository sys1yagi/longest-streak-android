package com.sys1yagi.longeststreakandroid.tool

import com.sys1yagi.longeststreakandroid.BuildConfig
import com.sys1yagi.longeststreakandroid.LongestStreakApplication
import com.sys1yagi.longeststreakandroid.db.EventLog
import com.sys1yagi.longeststreakandroid.db.OrmaDatabase
import com.sys1yagi.longeststreakandroid.model.Event
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.text.SimpleDateFormat

@RunWith(RobolectricGradleTestRunner::class)
@Config(application = LongestStreakApplication::class, constants = BuildConfig::class, sdk = intArrayOf(21))
class LongestStreakCounterTest {

    lateinit var database: OrmaDatabase

    val format = SimpleDateFormat("yyyy-MM-dd")

    @Before
    fun setUp() {
        database = OrmaDatabase.builder(RuntimeEnvironment.application)
                .name("test.db")
                .build()
    }

    @After
    fun tearDown() {
        database.connection.resetDatabase()
    }

    fun createEventLog(id: Long, date: String) {
        val event = EventLog()
        event.eventId = id
        event.name = "name"
        event.type = Event.Type.PUSH
        event.createdAt = format.parse(date).time
        database.insertIntoEventLog(event)
    }

    @Test
    fun count() {
        createEventLog(1, "2015-12-01")
        createEventLog(2, "2015-12-03")
        createEventLog(3, "2015-12-04")
        createEventLog(4, "2015-12-05")

        val now = format.parse("2015-12-06").time

        assertThat(LongestStreakCounter().count(database, now, "Asia/Tokyo")).isEqualTo(3)
    }

    @Test
    fun count2() {
        createEventLog(1, "2015-12-01")
        createEventLog(2, "2015-12-02")
        createEventLog(3, "2015-12-03")
        createEventLog(4, "2015-12-03")
        createEventLog(5, "2015-12-04")
        createEventLog(6, "2015-12-05")

        val now = format.parse("2015-12-05").time

        assertThat(LongestStreakCounter().count(database, now, "Asia/Tokyo")).isEqualTo(5)
    }

    @Test
    fun overMonth() {
        createEventLog(1, "2015-11-30")
        createEventLog(2, "2015-12-01")
        createEventLog(3, "2015-12-02")
        createEventLog(4, "2015-12-03")
        createEventLog(5, "2015-12-03")
        createEventLog(6, "2015-12-04")
        createEventLog(7, "2015-12-05")

        val now = format.parse("2015-12-05").time

        assertThat(LongestStreakCounter().count(database, now, "Asia/Tokyo")).isEqualTo(6)
    }
}
