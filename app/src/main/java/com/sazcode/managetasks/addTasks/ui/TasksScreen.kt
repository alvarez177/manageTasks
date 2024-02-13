package com.sazcode.managetasks.addTasks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.sazcode.managetasks.addTasks.ui.model.TaskModel

@Composable
fun TasksScreen(viewModel: AddTasksViewModel) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<TaskUiState>(
        initialValue = TaskUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                value = it
            }
        }
    }
    when(uiState) {
        is TaskUiState.Error -> {

        }
        TaskUiState.Loading -> {
            CircularProgressIndicator()
        }
        is TaskUiState.Success -> {
            val showDialog: Boolean by viewModel.showDialog.observeAsState(initial = false)
            Box(modifier = Modifier.fillMaxSize()) {
                AddTaskDialog(show = showDialog, onDismiss = { viewModel.onDialogClose() }, onTaskAdded = {
                    viewModel.onTaskCreated(it)
                })
                TaskList((uiState as TaskUiState.Success).tasks, viewModel)
                FabDialog(modifier = Modifier.align(Alignment.BottomEnd), viewModel)
            }
        }
    }
}

@Composable
fun FabDialog(modifier: Modifier, viewModel: AddTasksViewModel) {
    FloatingActionButton(
        modifier = modifier
            .padding(16.dp),
        onClick = { viewModel.onShowDialogClick() }) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add task")
    }
}

@Composable
fun TaskList(myTasks: List<TaskModel>, viewModel: AddTasksViewModel) {
    LazyColumn() {
        items(myTasks, key = { it.id }) { task ->
            TaskItem(taskModel = task, tasksViewModel = viewModel)
        }
    }
}

@Composable
fun TaskItem(taskModel: TaskModel, tasksViewModel: AddTasksViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    tasksViewModel.onItemRemove(taskModel)
                })
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp), text = taskModel.task
            )
            Checkbox(
                checked = taskModel.selected,
                onCheckedChange = { tasksViewModel.onCheckBoxSelected(taskModel) })
        }
    }
}

@Composable
fun AddTaskDialog(show: Boolean, onDismiss: () -> Unit, onTaskAdded: (String) -> Unit) {
    var myTask by remember {
        mutableStateOf("")
    }
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "Agregar tarea",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(value = myTask, singleLine = true, onValueChange = {
                    myTask = it
                })
                Spacer(modifier = Modifier.size(16.dp))
                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    onTaskAdded(myTask)
                    myTask = ""
                }) {
                    Text(text = "Confirmar")

                }
            }
        }
    }
}