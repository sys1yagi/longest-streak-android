package com.sys1yagi.longeststreakandroid.tool

import com.sys1yagi.longeststreakandroid.model.Commit
import com.sys1yagi.longeststreakandroid.model.Event

class PublicContributionJudgement {

    fun alreadyContributed(name: String, now: Long, events: List<Event>): Boolean {
        if (events.isEmpty()) {
            return false
        }

        return !events
                .filter {
                    event ->
                    // check type
                    if (!event.type.equals(Event.Type.PUSH)) {
                        return false
                    }

                    // check public contribution action
                    if (!isValidCommits(name, event.payload.commits)) {
                        return false
                    }

                    // check date
                    if (day(now) != day(event.createdAt.time)) {
                        return false
                    }

                    return true
                }
                .isEmpty()
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
