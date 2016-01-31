package com.sys1yagi.longeststreakandroid.tool

import com.sys1yagi.life_basic_android.tool.GsonProvider
import com.sys1yagi.longeststreakandroid.model.Event
import com.sys1yagi.longeststreakandroid.testtool.AssetsUtilForTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class PublicContributionJudgementTest {
    val publicContributionJudgement = PublicContributionJudgement()

    @Test
    fun empty() {
        assertThat(publicContributionJudgement.todayContributionCount("sys1yagi", 0, ArrayList())).isEqualTo(0)
    }

    @Test
    fun contribution() {
        val event = GsonProvider.instance.fromJson(AssetsUtilForTest.readString("ContributionEvent.json"), Event::class.java)
        val accessor = DateTimeFormatter.ISO_DATE_TIME.parse("2016-01-30T05:22:25Z");
        val now = LocalDateTime.from(accessor).toInstant(ZoneOffset.from(accessor)).toEpochMilli()

        assertThat(publicContributionJudgement.todayContributionCount("sys1yagi", now, listOf(event)))
                .isEqualTo(1)
    }

    @Test
    fun outOfDate() {
        val event = GsonProvider.instance.fromJson(AssetsUtilForTest.readString("ContributionEvent.json"), Event::class.java)
        val accessor = DateTimeFormatter.ISO_DATE_TIME.parse("2016-01-31T05:22:25Z");
        val now = LocalDateTime.from(accessor).toInstant(ZoneOffset.from(accessor)).toEpochMilli()

        assertThat(publicContributionJudgement.todayContributionCount("sys1yagi", now, listOf(event)))
                .isEqualTo(0)
    }

    @Test
    fun notContribution() {

    }
}