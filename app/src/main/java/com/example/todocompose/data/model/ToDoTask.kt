package com.example.todocompose.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todocompose.utils.Contants.DATABASE_TABLE

@Entity(tableName = DATABASE_TABLE)
data class ToDoTask(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,

    val priority: Priority,

    val description: String
)