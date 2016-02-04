package com.sys1yagi.longeststreakandroid.alarm

import com.sys1yagi.longeststreakandroid.BuildConfig
import com.sys1yagi.longeststreakandroid.LongestStreakApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.any
import org.mockito.Mockito.times
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.rule.PowerMockRule
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.text.SimpleDateFormat

@RunWith(RobolectricGradleTestRunner::class)
@Config(application = LongestStreakApplication::class, constants = BuildConfig::class, sdk = intArrayOf(21))
@PowerMockIgnore("org.mockito.*", "org.robolectric.*", "android.*", "com.jakewharton.threetenabp.*", "org.threeten.bp.*")
@PrepareForTest(PollingAlarmProcessorScheduler::class)
class PollingAlarmProcessorTest {

    @Rule @JvmField
    val rule = PowerMockRule()

    @Test
    fun nextAlarmTomorrow() {
        PowerMockito.mockStatic(PollingAlarmProcessorScheduler::class.java)

        val now = SimpleDateFormat("yyyy-MM-dd'T'mm:hh:ssZ").parse("2016-01-31T05:22:25+0900").time
        val tomorrow = SimpleDateFormat("yyyy-MM-dd'T'mm:hh:ssZ").parse("2016-02-01T00:00:00+0900").time

        val expected = tomorrow - now

        PollingAlarmProcessor.nextAlarmTomorrow(RuntimeEnvironment.application, now, "Asia/Tokyo")

        PowerMockito.verifyStatic(times(1))
        val captor = ArgumentCaptor.forClass(Int::class.java)
        PollingAlarmProcessorScheduler.scheduleRtcWakeup(any(), captor.capture())
        assertThat(captor.value).isEqualTo(expected.toInt())

    }
}
