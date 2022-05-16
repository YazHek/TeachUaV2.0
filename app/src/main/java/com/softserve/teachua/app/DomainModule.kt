package com.softserve.teachua.app

import android.content.Context
import com.softserve.teachua.data.retrofit.RetrofitClient
import com.softserve.teachua.data.retrofit.RetrofitApi
import com.softserve.teachua.data.retrofit.datasource.RemoteDataSource
import com.softserve.teachua.data.sharedpreferences.CurrentUserSharedPreferencesInterface
import com.softserve.teachua.data.sharedpreferences.SharedPreferences
import com.softserve.teachua.service.BannersService
import com.softserve.teachua.service.CategoriesService
import com.softserve.teachua.service.CurrentUserService
import com.softserve.teachua.service.UserService
import com.softserve.teachua.service.challenge.ChallengeUseCases
import com.softserve.teachua.service.challenge.ChallengesUseCasesInterface
import com.softserve.teachua.service.task.TaskUseCases
import com.softserve.teachua.service.task.TaskUseCasesInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun providesCurrentUserService(@ApplicationContext appContext: Context):CurrentUserService{
        return CurrentUserService(providesCurrentUserInterface(appContext))
    }

    @Singleton
    @Provides
    fun provideCategoriesService() : CategoriesService {
        return CategoriesService(providesRemoteDataSource())
    }

    @Singleton
    @Provides
    fun provideUserService() : UserService {
        return UserService(providesRemoteDataSource())
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

    @Singleton
    @Provides
    fun providesTaskUseCasesInterface():TaskUseCasesInterface{
        return TaskUseCases(providesRemoteDataSource())
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
    fun providesRetrofitService():RetrofitApi{
        return RetrofitClient().getClient()
    }

    @Singleton
    @Provides
    fun providesCurrentUserInterface( @ApplicationContext appContext: Context ):CurrentUserSharedPreferencesInterface{
        return SharedPreferences(appContext)
    }
}