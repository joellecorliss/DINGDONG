package com.wordle.dingdong.viewmodel

import androidx.lifecycle.asLiveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wordle.dingdong.data.TaskDao
import com.wordle.dingdong.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Shared [ViewModel] to provide data to the [TaskListFragment], [TaskDetailFragment],
 * and [AddTaskDialogFragment] and allow for interaction the the [Task]
 */
class TaskViewModel(private val taskDao: TaskDao) : ViewModel() {
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
            taskDao.update(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.delete(task)
        }
    }

    fun isValidEntry(title: String, description: String): Boolean {
        return title.isNotBlank() && description.isNotBlank()
    }
}

class TaskViewModelFactory(private val taskDao: TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(taskDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
