package com.example.todocompose.ui.screens.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import com.example.todocompose.R
import com.example.todocompose.components.DisplayAlertDialog
import com.example.todocompose.components.PriorityItem
import com.example.todocompose.data.model.Priority
import com.example.todocompose.ui.theme.*
import com.example.todocompose.ui.viewModels.SharedViewModel
import com.example.todocompose.utils.Action
import com.example.todocompose.utils.SearchAppBarState
import com.example.todocompose.utils.TrailingIconState

@Composable
fun  ListAppBar(
    sharedViewModel: SharedViewModel,
    searchAppBarState: SearchAppBarState,
    searchTextState: String
) {

    when(searchAppBarState){

        SearchAppBarState.CLOSED -> {

            DefaultListAppBar(
                onSearchClicked = {
                    sharedViewModel.searchAppBarState.value =
                        SearchAppBarState.OPENED
                },
                onSortClicked = { selectedPriority ->
                    sharedViewModel.persistSortingState(selectedPriority)
                },
                onDeleteAllCofirmed = {
                    //sharedViewModel.action.value = Action.DELETE_ALL
                    sharedViewModel.updateAction(newAction = Action.DELETE_ALL)
                }
            )

        }

        else -> {

            SearchAppBar(
                text = searchTextState,
                onTextChange = { newText ->
                    sharedViewModel.searchTextState.value = newText
                },
                onCloseClicked = {
                    sharedViewModel.searchAppBarState.value =
                        SearchAppBarState.CLOSED
                    sharedViewModel.searchTextState.value = ""
                },
                onSearchClicked = { query ->
                    sharedViewModel.searchDatabase(searchQuery = query)
                }
            )

        }

    }

}

@Composable
fun DefaultListAppBar(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllCofirmed: () -> Unit
){

    TopAppBar(

        title = {

            Text(
                text = stringResource(R.string.tasks),
                color = MaterialTheme.colors.topAppBarContentColor
            )

        },

        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,

        actions = {

            ListAppBarActions(
                onSearchClicked,
                onSortClicked,
                onDeleteAllCofirmed
            )

        }

    )

}

@Composable
fun ListAppBarActions(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllCofirmed: () -> Unit
){

    var openDialog by remember { mutableStateOf(false) }

    DisplayAlertDialog(
        title = stringResource(id = R.string.delete_all_tasks),
        message = stringResource(id = R.string.delete_all_tasks_confirmation),
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        onYesClicked = { onDeleteAllCofirmed() }
    )

    SearchAction(onSearchClicked = onSearchClicked)

    SortAction(onSortClicked = onSortClicked)

    DeleteAllAction(onDeleteAllCofirmed = {
        openDialog = true
    })

}

@Composable
fun SearchAction(
    
    onSearchClicked: () -> Unit
    
){
    
    IconButton(onClick = { onSearchClicked() } ) {

        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(R.string.search_tasks),
            tint = MaterialTheme.colors.topAppBarContentColor
        )

    }
    
}

@Composable
fun SortAction(
    onSortClicked: (Priority) -> Unit
){

    var exanded by remember { mutableStateOf(false) }

    IconButton(onClick = {

        exanded = true

    }) {

        Icon(
            imageVector = Icons.Filled.FilterList,
            contentDescription = stringResource(R.string.sort_tasks),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
        
        DropdownMenu(
            expanded = exanded,
            onDismissRequest = { exanded = false  }
        ) {

            Priority.values().slice(setOf(0,2,3)).forEach { priority ->


                DropdownMenuItem(
                    onClick = {
                        exanded = false
                        onSortClicked(priority)
                    }
                ) {

                    PriorityItem(priority = priority)

                }

            }

        }

    }

}

@Composable
fun DeleteAllAction(
    onDeleteAllCofirmed: () -> Unit
){

    var expanded by remember { mutableStateOf(false) }

    IconButton(
        onClick = { expanded = true }
    ) {
    
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.delete_all_action),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            })
        {

            DropdownMenuItem(
                onClick = {
                    onDeleteAllCofirmed()
                }
            ) {

                Text(
                    modifier = Modifier.padding(start = LARGE_PADDING),
                    text = stringResource(R.string.delete_all_action),
                    style = Typography.subtitle2
                )

            }

        }
        
    }
    
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
){

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_APP_BAR_HEIGHT),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.topAppBarBackgroundColor
    ) {

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = { text ->
                onTextChange(text)
            },
            placeholder = {
                Text(
                    modifier = Modifier.alpha(ContentAlpha.medium),
                    text = stringResource(R.string.search),
                    color = Color.White
                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colors.topAppBarContentColor,
                fontSize = Typography.subtitle1.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    modifier = Modifier.alpha(ContentAlpha.disabled),
                    onClick = {

                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(R.string.search_icon),
                        tint = MaterialTheme.colors.topAppBarContentColor
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if(text.isNotEmpty()){
                            onTextChange("")
                        }else{
                            onCloseClicked()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.close_icon),
                        tint = MaterialTheme.colors.topAppBarContentColor
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colors.topAppBarContentColor,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent
            )
        )

    }

}
