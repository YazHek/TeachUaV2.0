package com.softserve.teachua.service

import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.app.tools.mapper.toDistrict
import com.softserve.teachua.data.model.DistrictModel
import com.softserve.teachua.data.retrofit.RetrofitService
import javax.inject.Inject

class DistrictService @Inject constructor(
    private val retrofitService: RetrofitService,
) {

    suspend fun getAllDistricts(cityName: String): Resource<List<DistrictModel>> {

        val districtsResponse = retrofitService.getDistrictsByCityName(cityName)
        return if (districtsResponse.isSuccessful) {

            val districts = checkNotNull(districtsResponse.body()).map { it.toDistrict() }
            Resource.success(districts)
        } else
            Resource.error()

    }
}