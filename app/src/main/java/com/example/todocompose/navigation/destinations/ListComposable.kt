package com.example.todocompose.navigation.destinations

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.example.todocompose.ui.screens.list.ListScreen
import com.example.todocompose.ui.viewModels.SharedViewModel
import com.example.todocompose.utils.Action
import com.example.todocompose.utils.Contants.LIST_ARGUMENT_KEY
import com.example.todocompose.utils.Contants.LIST_SCREEN
import com.example.todocompose.utils.toAction

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.listComposable(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
){

    composable(
        route = LIST_SCREEN,
        arguments = listOf(navArgument(LIST_ARGUMENT_KEY){
            type = NavType.StringType
        })
    ){ navBackStackEntry ->

        val action: Action = navBackStackEntry.arguments?.getString(LIST_ARGUMENT_KEY).toAction()

        Log.d("ListComposable", action.name)

        var myAction by rememberSaveable {
            mutableStateOf(Action.NO_ACTION)
        }

        LaunchedEffect(key1 = myAction){
            if(action != myAction){
                myAction = action
                //sharedViewModel.action = action
                sharedViewModel.updateAction(action)
            }
        }

        val databaseAction: Action = sharedViewModel.action

        ListScreen(
            action = databaseAction,
            navigateToTaskScreen = navigateToTaskScreen,
            sharedViewModel = sharedViewModel
        )

    }

}