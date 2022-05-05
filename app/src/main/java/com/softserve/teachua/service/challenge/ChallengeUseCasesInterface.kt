package com.softserve.teachua.service.challenge

import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.data.model.ChallengeModel

interface ChallengesUseCasesInterface {

    suspend fun getChallenges(): Resource<List<ChallengeModel>>

    suspend fun getChallenge(id: Int): Resource<ChallengeModel>

}