package com.example.todocompose.utils

enum class Action {

    ADD,
    UPDATE,
    DELETE,
    DELETE_ALL,
    UNDO,
    NO_ACTION

}

//fun String?.toAction(): Action{
//    return when {
//        this == "ADD" -> {
//            Action.ADD
//        }
//        this == "UPDATE" -> {
//            Action.UPDATE
//        }
//        this == "DELETE" -> {
//            Action.DELETE
//        }
//        this == "DELETE_ALL" -> {
//            Action.DELETE_ALL
//        }
//        this == "UNDO" -> {
//            Action.UNDO
//        }
//        else -> {
//            Action.NO_ACTION
//        }
//    }
//}

fun String?.toAction(): Action{
    return if (this.isNullOrEmpty()) Action.NO_ACTION else Action.valueOf(this)
}