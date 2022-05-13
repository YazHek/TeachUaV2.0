package com.softserve.teachua.service

import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.app.tools.performGetFromRemote
import com.softserve.teachua.data.dto.UserLoggedDto
import com.softserve.teachua.data.dto.UserLoginDto
import com.softserve.teachua.data.retrofit.datasource.RemoteDataSource
import javax.inject.Inject

class LoggingService @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) {
    suspend fun getLoggedUser(userLoginDto: UserLoginDto): Resource<UserLoggedDto> {
        return performGetFromRemote(
            networkCall = { remoteDataSource.getLoggedUser(userLoginDto) },
            )

    }
}