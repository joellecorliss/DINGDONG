package com.dingdong.dingdong.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dingdong.dingdong.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for database interaction.
 */

@Dao
interface TaskDao {
    // coroutine
    // Room does not allow us to execute any database operations on the main thread - it will throw an error if we do
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM task_database WHERE id = :id")
    fun getTask(id: Long): Flow<Task>

    @Query("SELECT * FROM task_database ORDER BY task_date")
    fun getTasks(): Flow<List<Task>>
}
