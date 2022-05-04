package com.softserve.teachua.service

import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.app.tools.mapper.toBanner
import com.softserve.teachua.data.model.BannerModel
import com.softserve.teachua.data.retrofit.Common
import com.softserve.teachua.data.retrofit.RetrofitClient
import com.softserve.teachua.data.retrofit.RetrofitService
import retrofit2.Response
import javax.inject.Inject


class BannersService @Inject constructor(
    private val retrofitService : RetrofitService
) {


    suspend fun getAllBanners(): Resource<List<BannerModel>> {

        val bannersResponse = retrofitService.getAllBanners()
        return if (bannersResponse.isSuccessful) {
            val banners = checkNotNull(bannersResponse.body()).map { it.toBanner() }
            Resource.success(banners)
        } else {
            Resource.error()
        }

    }
}