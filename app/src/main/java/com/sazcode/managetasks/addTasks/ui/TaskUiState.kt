package com.sazcode.managetasks.addTasks.ui

import com.sazcode.managetasks.addTasks.ui.model.TaskModel

sealed interface TaskUiState {
    data object Loading : TaskUiState
    data class Error(val throwable: Throwable) : TaskUiState
    data class Success(val tasks: List<TaskModel>) : TaskUiState
}