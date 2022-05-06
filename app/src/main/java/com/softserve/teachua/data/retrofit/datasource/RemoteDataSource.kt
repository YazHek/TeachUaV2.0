package com.softserve.teachua.data.retrofit.datasource

import com.softserve.teachua.data.retrofit.RetrofitService
import javax.inject.Inject

//
//
//ALWAYS USE THIS
//
//
class RemoteDataSource @Inject constructor(private val retrofitService: RetrofitService) : BaseDataSource() {

    suspend fun getChallengeById(id : Int) = getResult { retrofitService.getChallengeById(id) }

    suspend fun getChallenges() = getResult { retrofitService.getChallenges() }

    suspend fun getTask(id : Int) = getResult { retrofitService.getTask(id) }

    suspend fun getAllBanners( ) = getResult { retrofitService.getAllBanners() }

    suspend fun getAllCategories ( ) = getResult { retrofitService.getAllCategories() }
}