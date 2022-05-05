package com.softserve.teachua.service

import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.app.tools.mapper.toCity
import com.softserve.teachua.data.model.CityModel
import com.softserve.teachua.data.retrofit.RetrofitService
import javax.inject.Inject

class CitiesService @Inject constructor(
    private val retrofitService: RetrofitService,
) {
    suspend fun getAllCities(): Resource<List<CityModel>> {

        val citiesResponse = retrofitService.getAllCities()
        return if (citiesResponse.isSuccessful) {

            val cities = checkNotNull(citiesResponse.body()).map { it.toCity() }
            Resource.success(cities)
        } else
            Resource.error()

    }
}