package com.softserve.teachua.data.retrofit.datasource

import com.softserve.teachua.data.dto.UserLoginDto
import com.softserve.teachua.data.retrofit.RetrofitApi
import javax.inject.Inject

//
//
//ALWAYS USE THIS
//
//

class RemoteDataSource @Inject constructor(private val retrofitService: RetrofitApi) :
    BaseDataSource() {

    suspend fun getChallengeById(id: Int) = getResult { retrofitService.getChallengeById(id) }

    suspend fun getChallenges() = getResult { retrofitService.getChallenges() }

    suspend fun getTask(id: Int) = getResult { retrofitService.getTask(id) }

    suspend fun getUserById(token: String, id: Int) =
        getResult { retrofitService.getUserById(token, id) }

    suspend fun getAllBanners() = getResult { retrofitService.getAllBanners() }

    suspend fun getAllCategories() = getResult { retrofitService.getAllCategories() }

    suspend fun getAllCities() = getResult { retrofitService.getAllCities() }

    suspend fun getLoggedUser(userLoginDto: UserLoginDto) =
        getResult { retrofitService.getLoggedUser(userLoginDto) }

    suspend fun getAllDistricts(cityName: String) =
        getResult { retrofitService.getDistrictsByCityName(cityName) }

    suspend fun getAllStations(cityName: String) =
        getResult { retrofitService.getStationsByCityName(cityName) }

}