package com.softserve.teachua.data.retrofit

import com.softserve.teachua.data.dto.BannersDto
import com.softserve.teachua.data.dto.CategoriesDro
import com.softserve.teachua.data.dto.ClubsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    @GET("banners")
    suspend fun getAllBanners(
    ): Response<List<BannersDto>>

    @GET("categories")
    suspend fun getAllCategories(
    ): Response<List<CategoriesDro>>


    @GET("clubs/search?")
    suspend fun getAllClubs(
        @Query("clubName")
        clubName: String? = "",
        @Query("cityName")
        cityName: String? = "Київ",
        @Query("isOnline")
        isOnline: Boolean? = false,
        @Query("categoryName")
        categoryName: String? = "",
        @Query("page")
        page: Int? = 0,
    ): Response<ClubsDto>


    @JvmSuppressWildcards
    @GET("clubs/search/advanced?")
    suspend fun getClubsByAdvancedSearch(
        @Query("name")
        name: String? = "",
        @Query("age")
        age: String? = null,
        @Query("cityName")
        cityName: String? = "Київ",
        @Query("districtName")
        districtName: String? = null,
        @Query("stationName")
        stationName: String? = null,
        @Query("categoriesName", encoded = true)
        categoriesName: List<String>? = null,
        @Query("isCenter")
        isCenter: Boolean? = null,
        @Query("isOnline")
        isOnline: Boolean? = null,
        @Query("sort")
        sort: String? = "name,asc",
        @Query("page")
        page: Int? = 0,
    ): Response<ClubsDto>
}