package com.softserve.teachua.service

import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.app.tools.performGetFromRemoteAndMapData
import com.softserve.teachua.app.tools.toDistrict
import com.softserve.teachua.data.model.DistrictModel
import com.softserve.teachua.data.retrofit.datasource.RemoteDataSource
import javax.inject.Inject

class DistrictService @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) {

    suspend fun getAllDistricts(cityName: String): Resource<List<DistrictModel>> {
        return performGetFromRemoteAndMapData(
            networkCall = { remoteDataSource.getAllDistricts(cityName) },
            map = { it.toDistrict() }
        )

    }
}