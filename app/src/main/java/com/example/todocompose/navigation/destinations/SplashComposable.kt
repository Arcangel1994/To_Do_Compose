package com.example.todocompose.navigation.destinations

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.example.todocompose.ui.screens.splash.SplashScreen
import com.example.todocompose.utils.Contants.SPLASH_SCREEN

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.splashComposable(
    navigateToListScreen: () -> Unit
){
    composable(
        route = SPLASH_SCREEN,
        exitTransition = {
            slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(
                    durationMillis = 2000
                )
            )
        }
    ){
        SplashScreen(navigateToListScreen = navigateToListScreen)
    }
}