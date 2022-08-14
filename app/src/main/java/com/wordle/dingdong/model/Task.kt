package com.wordle.dingdong.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

const val ABBR = 3

@Entity(tableName = "task_database")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "task_title") val taskTitle: String,
    @ColumnInfo(name = "task_desc") val taskDescription: String,
    @ColumnInfo(name = "task_date") val taskDate: Long,
) {
    fun getDay(): String {
        return SimpleDateFormat("EEEE").format(Date(taskDate)).take(ABBR)
    }

    fun getDate(): String {
        return SimpleDateFormat("dd").format(Date(taskDate))
    }

    // get first 3 chars of the month
    fun getMonth(): String {
        return SimpleDateFormat("MMMM").format(Date(taskDate)).take(ABBR)
    }

    fun getFullDateString(): String {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        return format.format(Date(taskDate))
    }
}
