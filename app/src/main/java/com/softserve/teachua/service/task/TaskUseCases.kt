package com.softserve.teachua.service.task

import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.app.tools.performGetFromRemote
import com.softserve.teachua.app.tools.performGetFromRemoteAndMapData
import com.softserve.teachua.data.dto.TaskDto
import com.softserve.teachua.data.retrofit.datasource.RemoteDataSource
import javax.inject.Inject

class TaskUseCases @Inject constructor(private val remoteDataSource: RemoteDataSource)
    :TaskUseCasesInterface {
    override suspend fun getTask(id: Int): Resource<TaskDto> {
        return performGetFromRemote (
            networkCall = {remoteDataSource.getTask(id)}
                )
    }
}