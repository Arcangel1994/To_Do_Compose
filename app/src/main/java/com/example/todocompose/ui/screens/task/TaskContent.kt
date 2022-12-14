package com.example.todocompose.ui.screens.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.todocompose.R
import com.example.todocompose.components.PriorityDropDown
import com.example.todocompose.data.model.Priority
import com.example.todocompose.ui.theme.LARGE_PADDING
import com.example.todocompose.ui.theme.MEDIUM_PADDING
import com.example.todocompose.ui.theme.Typography

@Composable
fun TaskContent(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescription: (String) -> Unit,
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background)
        .padding(all = LARGE_PADDING)
    )
    {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = { textChange ->
                onTitleChange(textChange)
            },
            label = {
                Text(
                    text = stringResource(R.string.title)
                )
            },
            textStyle = Typography.body1,
            singleLine = true
        )

        Divider(
            modifier = Modifier.height(MEDIUM_PADDING),
            color = MaterialTheme.colors.background
        )

        PriorityDropDown(
            priority = priority,
            onPrioritySelected = onPrioritySelected
        )


        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            value = description,
            onValueChange = { descriptionChnage ->
                onDescription(descriptionChnage)
            },
            label = {
                Text(
                    text = stringResource(R.string.description)
                )
            },
            textStyle = Typography.body1
        )

    }

}

@Composable
@Preview(showBackground = true)
fun TaskContentPreview(){
    TaskContent(
        title = "",
        onTitleChange = {  },
        description = "",
        onDescription = {  },
        priority = Priority.HIGH,
        onPrioritySelected = {  }
    )
}