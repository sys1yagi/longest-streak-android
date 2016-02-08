package com.sys1yagi.longeststreakandroid.db

import com.github.gfx.android.orma.annotation.Column
import com.github.gfx.android.orma.annotation.PrimaryKey
import com.github.gfx.android.orma.annotation.Table

@Table
class Settings {

    @PrimaryKey
    var id: Long = 0

    @Column
    lateinit var name: String

    @Column
    lateinit var email: String

    @Column
    lateinit var zoneId: String

    companion object {
        fun alreadyInitialized(database: OrmaDatabase): Boolean {
            return database.selectFromSettings().count() > 0
        }

        fun getRecord(database: OrmaDatabase): Settings {
            if (!alreadyInitialized(database)) {
                throw IllegalStateException("record not found")
            }
            return database.selectFromSettings().first()
        }

        fun getRecordAndAction(database: OrmaDatabase): Pair<Settings, (Settings) -> Settings> {
            var settings: Settings?
            var saveAction: (Settings) -> Settings
            if (!alreadyInitialized(database)) {
                settings = Settings()
                saveAction = {
                    database.insertIntoSettings(it)
                    it
                }
            } else {
                settings = getRecord(database)
                saveAction = {
                    database.updateSettings()
                            .idEq(it.id)
                            .name(it.name)
                            .email(it.email)
                            .zoneId(it.zoneId)
                            .execute()
                    it
                }
            }
            return Pair(settings, saveAction)
        }
    }
}
