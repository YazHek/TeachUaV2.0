package com.softserve.teachua.service

import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.app.tools.mapper.toCategory
import com.softserve.teachua.app.tools.performGetFromRemoteAndMapData
import com.softserve.teachua.data.model.CategoryModel
import com.softserve.teachua.data.retrofit.Common
import com.softserve.teachua.data.retrofit.RetrofitService
import com.softserve.teachua.data.retrofit.dataSource.RemoteDataSource
import kotlinx.coroutines.delay
import javax.inject.Inject

class CategoriesService @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
    suspend fun getAllCategories(): Resource<List<CategoryModel>> {
        return performGetFromRemoteAndMapData(
            networkCall = {remoteDataSource.getAllCategories()},
            map = {it.toCategory()}
        )
    }
}