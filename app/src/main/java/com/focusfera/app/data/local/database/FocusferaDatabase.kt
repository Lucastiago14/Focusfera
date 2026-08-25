package com.focusfera.app.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.focusfera.app.data.local.dao.SubjectDao
import com.focusfera.app.data.local.entity.SubjectEntity

@Database(
    entities = [
        SubjectEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FocusferaDatabase : RoomDatabase() {

    abstract fun subjectDao(): SubjectDao

    companion object {

        @Volatile
        private var INSTANCE: FocusferaDatabase? = null

        fun getDatabase(context: Context): FocusferaDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FocusferaDatabase::class.java,
                    "focusfera_database"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}