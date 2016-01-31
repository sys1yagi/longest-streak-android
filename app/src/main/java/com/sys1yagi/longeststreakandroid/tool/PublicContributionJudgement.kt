package com.sys1yagi.longeststreakandroid.tool

import com.sys1yagi.longeststreakandroid.model.Commit
import com.sys1yagi.longeststreakandroid.model.Event

class PublicContributionJudgement {

    fun todayContributionCount(name: String, now: Long, events: List<Event>): Int {
        if (events.isEmpty()) {
            return 0
        }

        return events
                .filter {
                    event ->
                    // check type
                    if (!event.type.equals(Event.Type.PUSH)) {
                        return@filter false
                    }

                    // check public contribution action
                    if (!isValidCommits(name, event.payload.commits)) {
                        return@filter false
                    }

                    // check date
                    if (day(now) != day(event.createdAt.time)) {
                        return@filter false
                    }

                    return@filter true
                }
                .size
    }

    fun isValidCommits(name: String, commits: List<Commit>): Boolean {
        return !commits.filter {
            commit ->
            return name.equals(commit.author.name)
        }.isEmpty()
    }

    fun day(time: Long): Long {
        return time / (1000L * 60L * 60L * 24L)
    }

}
