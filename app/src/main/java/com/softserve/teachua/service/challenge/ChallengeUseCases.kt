package com.softserve.teachua.service.challenge

import android.util.Log
import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.app.tools.mapper.toChallenge
import com.softserve.teachua.app.tools.mapper.toChallengeModelMap
import com.softserve.teachua.app.tools.performGetFromRemoteAndMapData
import com.softserve.teachua.data.model.ChallengeModel
import com.softserve.teachua.data.retrofit.dataSource.RemoteDataSource
import javax.inject.Inject

class ChallengeUseCases @Inject constructor(private val remoteDataSource: RemoteDataSource) : ChallengesUseCasesInterface{
    override suspend fun getChallenges(): Resource<List<ChallengeModel>> {
        val challenges = remoteDataSource.getChallenges()
        return performGetFromRemoteAndMapData(
            networkCall = {remoteDataSource.getChallenges()},
            map = { it.toChallengeModelMap() }
        )
    }

    override suspend fun getChallenge(id: Int): Resource<ChallengeModel> {
        return performGetFromRemoteAndMapData(
            networkCall = {remoteDataSource.getChallengeById(id)},
            map = {it.toChallenge()}
        )
    }


}