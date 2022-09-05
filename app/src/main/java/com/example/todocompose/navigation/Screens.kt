package com.example.todocompose.navigation

import androidx.navigation.NavHostController
import com.example.todocompose.utils.Action
import com.example.todocompose.utils.Contants.LIST_SCREEN
import com.example.todocompose.utils.Contants.SPLASH_SCREEN

class Screens(navHostController: NavHostController) {

    val splash: () -> Unit = {
        navHostController.navigate(route = "list/${Action.NO_ACTION.name}"){
            popUpTo(SPLASH_SCREEN){
                inclusive = true
            }
        }
    }

    val list: (Int) -> Unit = { taskId ->
        navHostController.navigate("task/$taskId")
    }

    val task: (Action) -> Unit = { action ->
        navHostController.navigate("list/${action.name}"){
            popUpTo(LIST_SCREEN){
                inclusive = true
            }
        }
    }

}