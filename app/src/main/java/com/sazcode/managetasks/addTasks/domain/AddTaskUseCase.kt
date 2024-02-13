package com.sazcode.managetasks.addTasks.domain

import com.sazcode.managetasks.addTasks.data.TaskRepository
import com.sazcode.managetasks.addTasks.ui.model.TaskModel
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(private val taskRepository: TaskRepository) {

    suspend operator fun invoke(taskModel: TaskModel) {
        taskRepository.add(taskModel = taskModel)
    }
}