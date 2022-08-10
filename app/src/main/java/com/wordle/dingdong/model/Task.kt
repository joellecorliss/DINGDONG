package com.wordle.dingdong.model

import androidx.room.*
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "task_database")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "task_title") val taskTitle: String,
    @ColumnInfo(name = "task_desc") val taskDescription: String,
    @ColumnInfo(name = "task_date") val taskDate: Long,
) {
    fun getDay(): String {
        return SimpleDateFormat("EEEE").format(Date(taskDate)).take(3)
    }
//
    fun getDate(): String {
        return SimpleDateFormat("dd").format(Date(taskDate))
    }

    //get first 3 chars of the month
    fun getMonth(): String {
        return SimpleDateFormat("MMMM").format(Date(taskDate)).take(3)
    }

    fun getFullDateString(): String {
        val format = SimpleDateFormat("dd/MM/yyyy",Locale.US)
        return format.format(Date(taskDate))
    }
}