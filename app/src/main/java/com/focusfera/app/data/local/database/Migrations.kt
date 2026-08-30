package com.focusfera.app.data.local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {

    override fun migrate(
        database: SupportSQLiteDatabase
    ) {
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `study_sessions` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `subjectId` INTEGER NOT NULL,
                `mode` TEXT NOT NULL,
                `plannedDuration` INTEGER NOT NULL,
                `studiedDuration` INTEGER NOT NULL,
                `startedAt` INTEGER NOT NULL,
                `endedAt` INTEGER,
                `completed` INTEGER NOT NULL,
                FOREIGN KEY(`subjectId`)
                    REFERENCES `subjects`(`id`)
                    ON UPDATE NO ACTION
                    ON DELETE RESTRICT
            )
            """.trimIndent()
        )

        database.execSQL(
            """
            CREATE INDEX IF NOT EXISTS
            `index_study_sessions_subjectId`
            ON `study_sessions` (`subjectId`)
            """.trimIndent()
        )
    }
}