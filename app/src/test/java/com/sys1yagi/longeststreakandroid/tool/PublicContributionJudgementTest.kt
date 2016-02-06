package com.sys1yagi.longeststreakandroid.tool

import com.google.common.reflect.TypeToken
import com.sys1yagi.longeststreakandroid.BuildConfig
import com.sys1yagi.longeststreakandroid.LongestStreakApplication
import com.sys1yagi.longeststreakandroid.model.Event
import com.sys1yagi.longeststreakandroid.testtool.AssetsUtilForTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.annotation.Config
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

@RunWith(RobolectricGradleTestRunner::class)
@Config(application = LongestStreakApplication::class, constants = BuildConfig::class, sdk = intArrayOf(21))
class PublicContributionJudgementTest {
    val publicContributionJudgement = PublicContributionJudgement()

    @Test
    fun empty() {
        assertThat(publicContributionJudgement.todayContributionCount("sys1yagi", "Asia/Tokyo", 0, ArrayList())).isEqualTo(0)
    }

    @Test
    fun contribution() {
        val event = GsonProvider.instance.fromJson(AssetsUtilForTest.readString("ContributionEvent.json"), Event::class.java)
        val accessor = DateTimeFormatter.ISO_DATE_TIME.parse("2016-01-30T05:22:25Z");
        val now = LocalDateTime.from(accessor).toInstant(ZoneOffset.from(accessor)).toEpochMilli()

        assertThat(publicContributionJudgement.todayContributionCount("sys1yagi", "Asia/Tokyo", now, listOf(event)))
                .isEqualTo(1)
    }

    @Test
    fun outOfDate() {
        val event = GsonProvider.instance.fromJson(AssetsUtilForTest.readString("ContributionEvent.json"), Event::class.java)
        val accessor = DateTimeFormatter.ISO_DATE_TIME.parse("2016-01-31T05:22:25Z");
        val now = LocalDateTime.from(accessor).toInstant(ZoneOffset.from(accessor)).toEpochMilli()

        assertThat(publicContributionJudgement.todayContributionCount("sys1yagi", "Asia/Tokyo", now, listOf(event)))
                .isEqualTo(0)
    }

    @Test
    fun multiCommitsPushEvent() {
        val event = GsonProvider.instance.fromJson(AssetsUtilForTest.readString("MultiCommitContributionEvent.json"), Event::class.java)
        val accessor = DateTimeFormatter.ISO_DATE_TIME.parse("2016-01-30T05:22:25Z");
        val now = LocalDateTime.from(accessor).toInstant(ZoneOffset.from(accessor)).toEpochMilli()

        assertThat(publicContributionJudgement.todayContributionCount("sys1yagi", "Asia/Tokyo", now, listOf(event)))
                .isEqualTo(2)
    }

    @Test
    fun complexMultiContributionEvent() {
        val type = object : TypeToken<List<Event>>() {}.type
        val events = GsonProvider.instance.fromJson<List<Event>>(AssetsUtilForTest.readString("ComplexMultiContributionEvents.json"), type)
        val accessor = DateTimeFormatter.ISO_DATE_TIME.parse("2016-02-05T05:22:25Z");
        val now = LocalDateTime.from(accessor).toInstant(ZoneOffset.from(accessor)).toEpochMilli()

        assertThat(publicContributionJudgement.todayContributionCount("sys1yagi", "Asia/Tokyo", now, events))
                .isEqualTo(22)
    }
}
