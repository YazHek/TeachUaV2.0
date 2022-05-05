package com.softserve.teachua.service

import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.app.tools.mapper.toStation
import com.softserve.teachua.data.model.StationModel
import com.softserve.teachua.data.retrofit.RetrofitService
import javax.inject.Inject

class StationsService @Inject constructor(
    private val retrofitService: RetrofitService,
) {
    suspend fun getAllStations(cityName: String): Resource<List<StationModel>> {

        val stationsResponse = retrofitService.getStationsByCityName(cityName)
        return if (stationsResponse.isSuccessful) {

            val categories = checkNotNull(stationsResponse.body()).map { it.toStation() }
            Resource.success(categories)
        } else
            Resource.error()

    }
}