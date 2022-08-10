package com.wordle.dingdong.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wordle.dingdong.model.Task

/**
 * Room database to persist data for the app.
 * This database stores a [Task] entity
 */
// TODO: create the database with all necessary annotations, methods, variables, etc.
@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_database").fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}