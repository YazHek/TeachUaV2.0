package com.softserve.teachua.service.challenge

import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.app.tools.toChallenge
import com.softserve.teachua.app.tools.toChallengeModelMap
import com.softserve.teachua.app.tools.performGetFromRemoteAndMapData
import com.softserve.teachua.data.model.ChallengeModel
import com.softserve.teachua.data.retrofit.datasource.RemoteDataSource
import javax.inject.Inject

class ChallengeUseCases @Inject constructor(private val remoteDataSource: RemoteDataSource) : ChallengesUseCasesInterface{
    override suspend fun getChallenges(): Resource<List<ChallengeModel>> {
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