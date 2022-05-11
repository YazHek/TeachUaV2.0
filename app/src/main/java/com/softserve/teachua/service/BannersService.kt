package com.softserve.teachua.service

import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.app.tools.toBanner
import com.softserve.teachua.app.tools.performGetFromRemoteAndMapData
import com.softserve.teachua.data.model.BannerModel
import com.softserve.teachua.data.retrofit.datasource.RemoteDataSource
import kotlinx.coroutines.delay
import javax.inject.Inject


class BannersService @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) {
    suspend fun getAllBanners(): Resource<List<BannerModel>> {
        return performGetFromRemoteAndMapData(
            networkCall = {
                remoteDataSource.getAllBanners()
            },
            map = { it.toBanner() }
        )
    }
}