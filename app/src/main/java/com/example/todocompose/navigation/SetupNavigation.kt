package com.example.todocompose.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.example.todocompose.navigation.destinations.listComposable
import com.example.todocompose.navigation.destinations.splashComposable
import com.example.todocompose.navigation.destinations.taskComposable
import com.example.todocompose.ui.viewModels.SharedViewModel
import com.example.todocompose.utils.Contants.SPLASH_SCREEN
import com.google.accompanist.navigation.animation.AnimatedNavHost

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SetupNavigation(
    navHostController: NavHostController,
    sharedViewModel: SharedViewModel
) {

    val screen = remember(navHostController) {
        Screens(navHostController = navHostController)
    }

    AnimatedNavHost(navController = navHostController, startDestination = SPLASH_SCREEN){

        splashComposable(
            navigateToListScreen = screen.splash
        )

        listComposable(
            navigateToTaskScreen = screen.list,
            sharedViewModel = sharedViewModel
        )

        taskComposable(
            sharedViewModel = sharedViewModel,
            navigateToTaskScreen = screen.task
        )

    }

}