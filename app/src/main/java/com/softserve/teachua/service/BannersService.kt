package com.softserve.teachua.service

import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.app.tools.mapper.toBanner
import com.softserve.teachua.app.tools.performGetFromRemoteAndMapData
import com.softserve.teachua.data.model.BannerModel
import com.softserve.teachua.data.retrofit.Common
import com.softserve.teachua.data.retrofit.RetrofitClient
import com.softserve.teachua.data.retrofit.RetrofitService
import com.softserve.teachua.data.retrofit.dataSource.RemoteDataSource
import kotlinx.coroutines.delay
import retrofit2.Response
import javax.inject.Inject


class BannersService @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {


    suspend fun getAllBanners(): Resource<List<BannerModel>> {
        return performGetFromRemoteAndMapData(
            networkCall = {
                remoteDataSource.getAllBanners()
            },
            map = {it.toBanner()}
        )
    }
}