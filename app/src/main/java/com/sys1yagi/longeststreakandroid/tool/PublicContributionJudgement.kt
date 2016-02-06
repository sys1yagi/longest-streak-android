package com.sys1yagi.longeststreakandroid.tool

import com.sys1yagi.longeststreakandroid.model.Commit
import com.sys1yagi.longeststreakandroid.model.Event
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

class PublicContributionJudgement {

    interface Judgement {
        fun isMyContribution(name: String, zoneId: String, now: Long, event: Event): Int
    }

    class PushEvent : Judgement {
        fun countValidCommits(name: String, commits: List<Commit>): Int {
            return commits.map { commit ->
                if (name.equals(commit.author.name)) 1 else 0
            }.reduce { i, j -> i + j }
        }

        override fun isMyContribution(name: String, zoneId: String, now: Long, event: Event): Int {
            // check type
            if (!event.type.equals(Event.Type.PUSH)) {
                return 0
            }

            // check date
            if (!isMatchDate(zoneId, now, event)) {
                return 0
            }

            // check public contribution action
            return countValidCommits(name, event.payload.commits)
        }
    }

    class IssuesEvent : Judgement {
        override fun isMyContribution(name: String, zoneId: String, now: Long, event: Event): Int {
            // check type
            if (!event.type.equals(Event.Type.ISSUES)) {
                return 0
            }
            // check date
            if (!isMatchDate(zoneId, now, event)) {
                return 0
            }

            return if (name.equals(event.payload.issue.user.login)) 1 else 0
        }
    }

    class PullRequestEvent : Judgement {
        override fun isMyContribution(name: String, zoneId: String, now: Long, event: Event): Int {
            // check type
            if (!event.type.equals(Event.Type.PULL_REQUEST)) {
                return 0
            }
            // check date
            if (!isMatchDate(zoneId, now, event)) {
                return 0
            }
            return if (name.equals(event.payload.pullRequest.user.login)) 1 else 0
        }
    }

    companion object {
        fun isMatchDate(zoneId: String, now: Long, event: Event): Boolean {
            return day(now, zoneId) == day(event.createdAt.time, zoneId)
        }

        fun day(time: Long, zoneId: String): Long {
            val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.of(zoneId))
            return dateTime.year * 10000L + dateTime.monthValue * 100L + dateTime.dayOfMonth
        }

        val judgements = arrayOf(
                PushEvent(),
                IssuesEvent(),
                PullRequestEvent()
        )
    }


    fun todayContributionCount(name: String, zoneId: String, now: Long, events: List<Event>): Int {
        if (events.isEmpty()) {
            return 0
        }
        return events
                .map {
                    event ->
                    judgements.forEach {
                        var count = it.isMyContribution(name, zoneId, now, event)
                        if ( count > 0) {
                            return@map count
                        }
                    }
                    0
                }
                .reduce { i, j -> i + j }
    }

}
