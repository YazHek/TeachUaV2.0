package com.softserve.teachua.service.task

import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.data.dto.TaskDto

interface TaskUseCasesInterface {

    suspend fun getTask(id : Int) : Resource<TaskDto>

}