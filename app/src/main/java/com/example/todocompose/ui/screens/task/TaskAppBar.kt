package com.example.todocompose.ui.screens.task

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.todocompose.R
import com.example.todocompose.components.DisplayAlertDialog
import com.example.todocompose.data.model.Priority
import com.example.todocompose.data.model.ToDoTask
import com.example.todocompose.ui.theme.topAppBarBackgroundColor
import com.example.todocompose.ui.theme.topAppBarContentColor
import com.example.todocompose.utils.Action

@Composable
fun TaskAppBar(
    selectedTask: ToDoTask?,
    navigateToListScreens: (Action) -> Unit
){

    if(selectedTask == null){
        NewTaskAppBar(
            navigateToListScreens = navigateToListScreens
        )
    }else{
        ExistingTaskAppBar(
            selectedTask = selectedTask,
            navigateToListScreens = navigateToListScreens
        )
    }
}

@Composable
fun NewTaskAppBar(
    navigateToListScreens: (Action) -> Unit
){

    TopAppBar(
        navigationIcon = {

            BackAction(onBackClicked = navigateToListScreens)

        },
        title = {
            Text(
                text = stringResource(R.string.add_task),
                color = MaterialTheme.colors.topAppBarContentColor
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        actions = {

            AddAction(onAddClicked = navigateToListScreens)

        }
    )

}

@Composable
fun BackAction(
    onBackClicked: (Action) -> Unit
){

    IconButton(onClick = { 
        
        onBackClicked(Action.NO_ACTION)
        
    }) {
        
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back_arrow),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
        
    }

}

@Composable
fun AddAction(
    onAddClicked: (Action) -> Unit
){

    IconButton(onClick = {

        onAddClicked(Action.ADD)

    }) {

        Icon(
            imageVector = Icons.Filled.Save,
            contentDescription = stringResource(R.string.add),
            tint = MaterialTheme.colors.topAppBarContentColor
        )

    }

}

@Composable
fun ExistingTaskAppBar(
    selectedTask: ToDoTask,
    navigateToListScreens: (Action) -> Unit
){

    TopAppBar(
        navigationIcon = {

            CloseAction(onCloseClicked = navigateToListScreens)

        },
        title = {
            Text(
                text = selectedTask.title,
                color = MaterialTheme.colors.topAppBarContentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        actions = {

            ExistingTaskAppBarAction(
                selectedTask = selectedTask,
                navigateToListScreens = navigateToListScreens
            )

        }
    )

}

@Composable
fun ExistingTaskAppBarAction(
    selectedTask: ToDoTask,
    navigateToListScreens: (Action) -> Unit
){

    var openDialog by remember {
        mutableStateOf(false)
    }

    DisplayAlertDialog(
        title = stringResource(id = R.string.delete_task, selectedTask.title),
        message = stringResource(id = R.string.delete_task_confirmation, selectedTask.title),
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        onYesClicked = {
            navigateToListScreens(Action.DELETE)
        }
    )

    DeleteAction(onDeleteClicked = {
        openDialog = true
    })

    UpdateAction(navigateToListScreens)
}

@Composable
fun CloseAction(
    onCloseClicked: (Action) -> Unit
){

    IconButton(onClick = {

        onCloseClicked(Action.NO_ACTION)

    }) {

        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(R.string.close_icon),
            tint = MaterialTheme.colors.topAppBarContentColor
        )

    }

}

@Composable
fun DeleteAction(
    onDeleteClicked: () -> Unit
){

    IconButton(onClick = {

        onDeleteClicked()

    }) {

        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(R.string.delete),
            tint = MaterialTheme.colors.topAppBarContentColor
        )

    }

}

@Composable
fun UpdateAction(
    onUpdateClicked: (Action) -> Unit
){

    IconButton(onClick = {

        onUpdateClicked(Action.UPDATE)

    }) {

        Icon(
            imageVector = Icons.Filled.Save,
            contentDescription = stringResource(R.string.update),
            tint = MaterialTheme.colors.topAppBarContentColor
        )

    }

}

@Composable
@Preview
fun NewTaskAppBarPreview(){
    NewTaskAppBar(navigateToListScreens = {  })
}

@Composable
@Preview
fun ExistingTaskAppBarPreview(){
    ExistingTaskAppBar(
        selectedTask = ToDoTask(
            0,
            "Miguel Muñoz",
            Priority.HIGH,
            "Some Ramdon Text"
        ),
        navigateToListScreens = {  }
    )
}