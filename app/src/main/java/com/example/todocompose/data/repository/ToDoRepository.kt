package com.example.todocompose.data.repository

import com.example.todocompose.data.ToDoDao
import com.example.todocompose.data.model.ToDoTask
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class ToDoRepository @Inject constructor(private val toDoDao: ToDoDao) {

    val getAllTasks: Flow<List<ToDoTask>> = toDoDao.getAllTasks()

    val sortByHighPriority: Flow<List<ToDoTask>> = toDoDao.sortByHighPriority()
    val sortByLowPriority: Flow<List<ToDoTask>> = toDoDao.sortByLowPriority()

    fun getSelectedTask(taskId: Int): Flow<ToDoTask>{
        return toDoDao.getSelectedTask(taskId)
    }

    suspend fun addTask (toDoTask: ToDoTask){
        toDoDao.addTask(toDoTask)
    }

    suspend fun updateTask (toDoTask: ToDoTask){
        toDoDao.updateTask(toDoTask)
    }

    suspend fun deleteTask (toDoTask: ToDoTask){
        toDoDao.deleteTask(toDoTask)
    }

    suspend fun deleteAllTasks (){
        toDoDao.deleteAllTasks()
    }

    fun searchDatabase(searchQuery: String): Flow<List<ToDoTask>> {
        return toDoDao.searchDatabase(searchQuery)
    }

}