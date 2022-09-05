package com.example.todocompose.ui.screens.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.todocompose.R
import com.example.todocompose.data.model.ToDoTask
import com.example.todocompose.ui.theme.fabBackgroundColor
import com.example.todocompose.ui.viewModels.SharedViewModel
import com.example.todocompose.utils.Action
import com.example.todocompose.utils.RequestState
import com.example.todocompose.utils.SearchAppBarState
import kotlinx.coroutines.launch

@Composable
fun ListScreen(
    action: Action,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
){

    LaunchedEffect(key1 = true){
        sharedViewModel.getAllTasks()
        sharedViewModel.readSortState()
    }

    LaunchedEffect(key1 = action){
        sharedViewModel.handleDatabaseActions(action = action)
    }

    val allTasks: RequestState<List<ToDoTask>> by sharedViewModel.allTasks.collectAsState()
    val searchedTasks: RequestState<List<ToDoTask>> by sharedViewModel.searchedTasks.collectAsState()

    val sortState by sharedViewModel.sortState.collectAsState()
    val lowPriorityTasks by sharedViewModel.lowPriority.collectAsState()
    val highPriorityTasks by sharedViewModel.highPriority.collectAsState()

    val searchAppBarState: SearchAppBarState by sharedViewModel.searchAppBarState
    val searchTextState: String by sharedViewModel.searchTextState

    val scaffoldState = rememberScaffoldState()

    DisplaySnackBar(
        scaffoldState = scaffoldState,
        onComplete = {
                        //sharedViewModel.action.value = it
                        sharedViewModel.updateAction(newAction = it)
                     },
        onUndoClicked = {
            //sharedViewModel.action.value = it
            sharedViewModel.updateAction(newAction = it)
        },
        taskTitle = sharedViewModel.title.value,
        action = action
    )

    Scaffold(

        scaffoldState = scaffoldState,

        topBar = {

            ListAppBar(
                sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState
            )

        },

        content = { padding ->
            Column(modifier = Modifier.padding(padding)){}
            ListContent(
                allTasks = allTasks,
                searchedTasks = searchedTasks,
                lowPriorityTasks = lowPriorityTasks,
                highPriorityTasks = highPriorityTasks,
                sortState = sortState,
                searchAppBarState = searchAppBarState,
                onSwipeToDelete = { action, task ->
                    //sharedViewModel.action = action
                    sharedViewModel.updateAction(newAction = action)
                    sharedViewModel.updateTaskFields(selectedTask = task)
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                },
                navigateToTaskScreen = navigateToTaskScreen
            )
        },

        floatingActionButton = {

            ListFab(onFabClicked = navigateToTaskScreen)

        }

    )

}

@Composable
fun ListFab(onFabClicked: (taskId: Int) -> Unit){

    FloatingActionButton(

        onClick = {

            onFabClicked(-1)
        },

        backgroundColor = MaterialTheme.colors.fabBackgroundColor

    ) {

        Icon(
            imageVector = Icons.Filled.Add,
            tint = Color.White,
            contentDescription = stringResource(id = R.string.add_button))

    }

}

private fun setMessage(action: Action, taskTitle: String): String{
    return when(action){

        Action.DELETE_ALL -> {
            "All Tasks Remove."
        }

        else -> {
            "${action.name}: $taskTitle"
        }

    }
}

@Composable
fun DisplaySnackBar(
    scaffoldState: ScaffoldState,
    onComplete: (Action) -> Unit,
    onUndoClicked: (Action) -> Unit,
    taskTitle: String,
    action: Action
){

    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = action){
        if(action != Action.NO_ACTION){

            scope.launch {

                val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = setMessage(action = action, taskTitle = taskTitle),
                    actionLabel = setActionLabel(action = action)
                )
                undoDeletedTask(
                    action = action,
                    snackBarResult = snackBarResult,
                    onUndoClicked = onUndoClicked
                )

            }

            onComplete(Action.NO_ACTION)

        }
    }

}

private fun setActionLabel(action: Action): String{
    return if(action.name == "DELETE"){
        "UNDO"
    }else{
        "OK"
    }
}

private fun undoDeletedTask(
    action: Action,
    snackBarResult: SnackbarResult,
    onUndoClicked: (Action) -> Unit
){

    if (snackBarResult == SnackbarResult.ActionPerformed &&
            action == Action.DELETE){
        onUndoClicked(Action.UNDO)
    }

}