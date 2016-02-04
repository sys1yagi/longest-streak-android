package com.sys1yagi.longeststreakandroid.tool

import com.sys1yagi.longeststreakandroid.model.Commit
import com.sys1yagi.longeststreakandroid.model.Event
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

class PublicContributionJudgement {

    fun todayContributionCount(name: String, zoneId: String, now: Long, events: List<Event>): Int {
        if (events.isEmpty()) {
            return 0
        }
        return events
                .map {
                    event ->
                    // check type
                    if (!event.type.equals(Event.Type.PUSH)) {
                        return@map 0
                    }

                    // check date
                    if (day(now, zoneId) != day(event.createdAt.time, zoneId)) {
                        return@map 0
                    }

                    // check public contribution action
                    return@map countValidCommits(name, event.payload.commits)
                }
                .reduce { i, j -> i + j }
    }

    fun countValidCommits(name: String, commits: List<Commit>): Int {
        return commits.map { commit ->
            if (name.equals(commit.author.name)) 1 else 0
        }.reduce { i, j -> i + j }
    }

    fun day(time: Long, zoneId: String): Long {
        val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.of(zoneId))
        return dateTime.year * 10000L + dateTime.monthValue * 100L + dateTime.dayOfMonth
    }

}
