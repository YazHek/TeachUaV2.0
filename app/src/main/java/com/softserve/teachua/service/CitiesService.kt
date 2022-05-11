package com.softserve.teachua.service

import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.app.tools.performGetFromRemoteAndMapData
import com.softserve.teachua.app.tools.toCity
import com.softserve.teachua.data.model.CityModel
import com.softserve.teachua.data.retrofit.datasource.RemoteDataSource
import javax.inject.Inject

class CitiesService @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) {
    suspend fun getAllCities(): Resource<List<CityModel>> {
        return performGetFromRemoteAndMapData(
            networkCall = { remoteDataSource.getAllCities() },
            map = { it.toCity() }
        )

    }
}