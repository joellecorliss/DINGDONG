package com.wordle.dingdong.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.wordle.dingdong.data.TaskDao
import com.wordle.dingdong.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Shared [ViewModel] to provide data to the [TaskListFragment], [TaskDetailFragment],
 * and [AddTaskDialogFragment] and allow for interaction the the [Task]
 */

class TaskViewModel(private val taskDao: TaskDao): ViewModel()  {
    val allTasks: LiveData<List<Task>> = taskDao.getTasks().asLiveData()

    // TODO : create method that takes id: Long as a parameter and retrieve a Task from the
    //  database by id via the DAO.
    fun retrieveTask(id: Long): LiveData<Task> {
        return taskDao.getTask(id).asLiveData()
    }

    fun addTask(
        name: String,
        description: String,
        date: Long,
    ) {
        val task = Task(
            taskTitle = name,
            taskDescription = description,
            taskDate = date,
        )

        // TODO: launch a coroutine and call the DAO method to add a task to the database within it
        viewModelScope.launch {
            taskDao.insert(task)
        }
    }

    fun updateTask(
        id: Long,
        name: String,
        description: String,
        date: Long,
    ) {
        val task = Task(
            id = id,
            taskTitle = name,
            taskDescription = description,
            taskDate = date,
        )
        viewModelScope.launch(Dispatchers.IO) {
            // TODO: call the DAO method to update a task to the database here
            taskDao.update(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            // TODO: call the DAO method to delete a task to the database here
            taskDao.delete(task)
        }
    }

    fun isValidEntry(title: String, description: String): Boolean {
        return title.isNotBlank() && description.isNotBlank()
    }
}

// TODO: create a view model factory that takes a TaskDao as a property and
//  creates a TaskViewModel
class TaskViewModelFactory(private val taskDao: TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(taskDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}