package com.focusfera.app.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.focusfera.app.data.local.dao.SubjectDao
import com.focusfera.app.data.local.entity.SubjectEntity
import com.focusfera.app.data.local.entity.StudySessionEntity
import com.focusfera.app.data.local.dao.StudySessionDao

@Database(
    entities = [
        SubjectEntity::class,
        StudySessionEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class FocusferaDatabase : RoomDatabase() {

    abstract fun subjectDao(): SubjectDao

    abstract fun studySessionDao(): StudySessionDao

    companion object {

        @Volatile
        private var INSTANCE: FocusferaDatabase? = null

        fun getDatabase(context: Context): FocusferaDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FocusferaDatabase::class.java,
                    "focusfera_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}