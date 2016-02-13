package com.sys1yagi.longeststreakandroid.db

import com.github.gfx.android.orma.annotation.Column
import com.github.gfx.android.orma.annotation.PrimaryKey
import com.github.gfx.android.orma.annotation.Table
import com.sys1yagi.longeststreakandroid.model.Event

import java.util.Date

@Table
class EventLog {
    @PrimaryKey
    var id: Long = 0

    @Column
    lateinit var name: String

    @Column
    lateinit var createdAt: Date

    @Column
    lateinit var type: Event.Type

}
