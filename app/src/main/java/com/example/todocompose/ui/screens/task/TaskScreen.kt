package com.example.todocompose.ui.screens.task

import android.content.Context
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.todocompose.R
import com.example.todocompose.data.model.Priority
import com.example.todocompose.data.model.ToDoTask
import com.example.todocompose.ui.viewModels.SharedViewModel
import com.example.todocompose.utils.Action

@Composable
fun TaskScreen(
    sharedViewModel: SharedViewModel,
    selectedTask: ToDoTask?,
    navigateToListScreens: (Action) -> Unit
) {

    val title: String by sharedViewModel.title
    val description: String by sharedViewModel.description
    val priority: Priority by sharedViewModel.priority

    val context: Context = LocalContext.current

//    backHandler(onBackPressed = {
//        navigateToListScreens(Action.NO_ACTION)
//    })
    BackHandler {
        navigateToListScreens(Action.NO_ACTION)
    }

    Scaffold(

        topBar = {

            TaskAppBar(
                selectedTask = selectedTask,
                navigateToListScreens = { action ->
                    if(action == Action.NO_ACTION){
                        navigateToListScreens(action)
                    }else{
                        if(sharedViewModel.validateFields()){
                            navigateToListScreens(action)
                        }else{
                            displayToast(context = context)
                        }
                    }
                }
            )

        },

        content = { padding ->
            Column(modifier = androidx.compose.ui.Modifier.padding(padding)){}
            TaskContent(
                title = title,
                onTitleChange = {
                    sharedViewModel.updateTitle(it)
                },
                description = description,
                onDescription = {
                    sharedViewModel.description.value = it
                },
                priority = priority,
                onPrioritySelected = {
                    sharedViewModel.priority.value = it
                }
            )
        }

    )

}

fun displayToast(context: Context) =
    Toast.makeText(
        context,
        context.getString(R.string.fields_empty),
        Toast.LENGTH_SHORT
    ).show()

//@Composable
//fun backHandler(
//    backDispatcher: OnBackPressedDispatcher? = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
//    onBackPressed: () -> Unit
//){
//
//    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)
//
//    val backCallBack = remember {
//        object : OnBackPressedCallback(true){
//            override fun handleOnBackPressed() {
//
//                currentOnBackPressed()
//
//            }
//
//        }
//    }
//
//    DisposableEffect(key1 = backDispatcher){
//        backDispatcher?.addCallback(backCallBack)
//
//        onDispose{
//
//            backCallBack.remove()
//
//        }
//    }
//
//}