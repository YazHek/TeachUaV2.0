package com.softserve.teachua.app

import com.softserve.teachua.data.retrofit.RetrofitClient
import com.softserve.teachua.data.retrofit.RetrofitService
import com.softserve.teachua.data.retrofit.dataSource.RemoteDataSource
import com.softserve.teachua.service.BannersService
import com.softserve.teachua.service.CategoriesService
import com.softserve.teachua.service.challenge.ChallengeUseCases
import com.softserve.teachua.service.challenge.ChallengesUseCasesInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModule () {


    //
    //Service
    //
    @Singleton
    @Provides
    fun provideCategoriesService() : CategoriesService {
        return CategoriesService(providesRemoteDataSource())
    }

    @Singleton
    @Provides
    fun provideBannersService() : BannersService {
        return BannersService(providesRemoteDataSource())
    }

    @Singleton
    @Provides
    fun providesChallengeUseCasesInterface():ChallengesUseCasesInterface{
        return ChallengeUseCases(providesRemoteDataSource())
    }

    //
    //Remote Data Source (RETROFIT, RemoteDataSource)
    //
    @Singleton
    @Provides
    fun providesRemoteDataSource():RemoteDataSource{
        return RemoteDataSource(providesRetrofitService())
    }

    @Singleton
    @Provides
    fun providesRetrofitService():RetrofitService{
        return RetrofitClient().getClient()
    }

}