package com.sys1yagi.longeststreakandroid.db

import com.github.gfx.android.orma.annotation.Column
import com.github.gfx.android.orma.annotation.OnConflict
import com.github.gfx.android.orma.annotation.PrimaryKey
import com.github.gfx.android.orma.annotation.Table
import com.sys1yagi.longeststreakandroid.model.Event

import java.util.Date

@Table
class EventLog {

    @PrimaryKey
    var id: Long = 0

    @Column(uniqueOnConflict = OnConflict.IGNORE)
    var eventId: Long = 0

    @Column
    lateinit var name: String

    @Column(indexed = true)
    var createdAt: Long = 0

    @Column
    lateinit var type: Event.Type


    companion object {
        fun toEventLog(name: String, event: Event): EventLog {
            val eventLog = EventLog()
            eventLog.eventId = event.id
            eventLog.name = name
            eventLog.createdAt = event.createdAt.time
            eventLog.type = event.type
            return eventLog
        }
    }
}
