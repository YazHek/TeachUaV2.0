package com.softserve.teachua.service

import com.softserve.teachua.mapper.toBanner
import com.softserve.teachua.model.BannerModel
import com.softserve.teachua.retrofit.Common

class BannersService() {
    var retrofitService = Common.retrofitService

    suspend fun getAllBanners(): List<BannerModel> {

        lateinit var  banners: List<BannerModel>
        val bannersResponse = retrofitService.getAllBanners()
        if (bannersResponse.isSuccessful) {

            banners = checkNotNull(bannersResponse.body()).map { it.toBanner() }
        }
        return banners

    }
}