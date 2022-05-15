package com.softserve.teachua.service

import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.app.tools.performGetFromRemote
import com.softserve.teachua.data.dto.UserDto
import com.softserve.teachua.data.retrofit.datasource.RemoteDataSource
import javax.inject.Inject

class UserService @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) {
    suspend fun getUserById(token: String, id: Int): Resource<UserDto> {
        return performGetFromRemote(
            networkCall = { remoteDataSource.getUserById(token, id) },
            )
    }
}