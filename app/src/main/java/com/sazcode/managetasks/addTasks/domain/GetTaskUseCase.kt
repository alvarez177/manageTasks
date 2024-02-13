package com.sazcode.managetasks.addTasks.domain

import com.sazcode.managetasks.addTasks.data.TaskRepository
import com.sazcode.managetasks.addTasks.ui.model.TaskModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    operator fun invoke():Flow<List<TaskModel>> {
        return repository.tasks
    }
}