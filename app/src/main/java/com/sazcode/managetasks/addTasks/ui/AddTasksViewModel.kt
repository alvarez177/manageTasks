package com.sazcode.managetasks.addTasks.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sazcode.managetasks.addTasks.domain.AddTaskUseCase
import com.sazcode.managetasks.addTasks.domain.DeleteTaskUseCase
import com.sazcode.managetasks.addTasks.domain.GetTaskUseCase
import com.sazcode.managetasks.addTasks.domain.UpdateTaskUseCase
import com.sazcode.managetasks.addTasks.ui.TaskUiState.Error
import com.sazcode.managetasks.addTasks.ui.TaskUiState.Success
import com.sazcode.managetasks.addTasks.ui.model.TaskModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTasksViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    getTaskUseCase: GetTaskUseCase
) : ViewModel() {

    val uiState: StateFlow<TaskUiState> =
        getTaskUseCase().map(::Success).catch { Error(it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TaskUiState.Loading)
    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    fun onDialogClose() {
        _showDialog.value = false
    }

    fun onTaskCreated(taskContent: String) {
        //_showDialog.value = false
        //_tasks.add(TaskModel(task = taskContent))

        viewModelScope.launch {
            addTaskUseCase.invoke(TaskModel(task = taskContent))
        }
    }

    fun onShowDialogClick() {
        _showDialog.value = true
    }

    fun onCheckBoxSelected(taskModel: TaskModel) {
        /*val index = _tasks.indexOf(taskModel)
        _tasks[index] = _tasks[index].let {
            it.copy(selected = !it.selected)
        }*/
        viewModelScope.launch {
            updateTaskUseCase(taskModel.copy(selected = !taskModel.selected))
        }
    }

    fun onItemRemove(taskModel: TaskModel) {
        /*val task = _tasks.find { it.id == taskModel.id }
        _tasks.remove(task)*/
        viewModelScope.launch {
            deleteTaskUseCase(taskModel)
        }
    }
}