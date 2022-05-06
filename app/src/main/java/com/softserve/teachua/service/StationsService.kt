package com.softserve.teachua.service

import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.app.tools.mapper.toStation
import com.softserve.teachua.app.tools.performGetFromRemoteAndMapData
import com.softserve.teachua.data.model.StationModel
import com.softserve.teachua.data.retrofit.datasource.RemoteDataSource
import javax.inject.Inject

class StationsService @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) {
    suspend fun getAllStations(cityName: String): Resource<List<StationModel>> {
        return performGetFromRemoteAndMapData(
            networkCall = { remoteDataSource.getAllStations(cityName) },
            map = { it.toStation() }
        )

    }
}