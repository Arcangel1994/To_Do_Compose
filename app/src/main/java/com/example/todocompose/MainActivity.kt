package com.example.todocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavHostController
import com.example.todocompose.navigation.SetupNavigation
import com.example.todocompose.ui.theme.ToDoComposeTheme
import com.example.todocompose.ui.viewModels.SharedViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoComposeTheme {
                // A surface container using the 'background' color from the theme

                navController = rememberAnimatedNavController()

                SetupNavigation(
                    navHostController = navController,
                    sharedViewModel = sharedViewModel
                )

            }
        }
    }
}
