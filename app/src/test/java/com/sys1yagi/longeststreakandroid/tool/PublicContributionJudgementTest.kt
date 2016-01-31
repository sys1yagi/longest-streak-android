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
        assertThat(publicContributionJudgement.alreadyContributed("sys1yagi", 0, ArrayList())).isFalse()
    }

    @Test
    fun contribution() {
        val event = GsonProvider.instance.fromJson(AssetsUtilForTest.readString("ContributionEvent.json"), Event::class.java)
        val accessor = DateTimeFormatter.ISO_DATE_TIME.parse("2016-01-30T05:22:25Z");
        val now = LocalDateTime.from(accessor).toInstant(ZoneOffset.from(accessor)).toEpochMilli()

        assertThat(publicContributionJudgement.alreadyContributed("sys1yagi", now, listOf(event)))
                .isTrue()
    }

    @Test
    fun outOfDate() {
        val event = GsonProvider.instance.fromJson(AssetsUtilForTest.readString("ContributionEvent.json"), Event::class.java)
        val accessor = DateTimeFormatter.ISO_DATE_TIME.parse("2016-01-31T05:22:25Z");
        val now = LocalDateTime.from(accessor).toInstant(ZoneOffset.from(accessor)).toEpochMilli()

        assertThat(publicContributionJudgement.alreadyContributed("sys1yagi", now, listOf(event)))
                .isFalse()
    }

    @Test
    fun notContribution() {

    }
}
