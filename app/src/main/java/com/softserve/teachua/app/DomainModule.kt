package com.softserve.teachua.app

import com.softserve.teachua.data.retrofit.RetrofitClient
import com.softserve.teachua.data.retrofit.RetrofitService
import com.softserve.teachua.service.BannersService
import com.softserve.teachua.service.CategoriesService
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
        return CategoriesService(providesRetrofitService())
    }

    @Singleton
    @Provides
    fun provideBannersService() : BannersService {
        return BannersService(providesRetrofitService())
    }

    @Singleton
    @Provides
    fun providesRetrofitService():RetrofitService{
        return RetrofitClient().getClient()
    }

}