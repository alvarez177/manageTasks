package com.sazcode.managetasks.addTasks.domain

import com.sazcode.managetasks.addTasks.data.TaskRepository
import com.sazcode.managetasks.addTasks.ui.model.TaskModel
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(private val repository: TaskRepository) {

    suspend operator fun invoke(taskModel: TaskModel) {
        repository.deleteTask(taskModel)
    }
}