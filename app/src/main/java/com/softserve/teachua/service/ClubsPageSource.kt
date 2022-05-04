package com.softserve.teachua.service

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.softserve.teachua.dto.AdvancedSearchClubDto
import com.softserve.teachua.dto.SearchClubDto
import com.softserve.teachua.mapper.toClub
import com.softserve.teachua.model.ClubModel
import com.softserve.teachua.retrofit.RetrofitService
import retrofit2.HttpException

class ClubsPageSource(
    private val service: RetrofitService,
    var searchClubDto: SearchClubDto,
    var advancedSearchClubDto: AdvancedSearchClubDto,
) : PagingSource<Int, ClubModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ClubModel> {
        // println("length " + MainActivity().cityName.length)
        val clubName = ""
        var cityName = searchClubDto.cityName
        val isOnline = false
        val categoryName = ""
        val page = params.key ?: 0
        var pageSize = 8

        println(advancedSearchClubDto.categoriesName)
        println("in datasource " + searchClubDto.cityName)
        println("in datasource" + searchClubDto.cityName.length)

        if (cityName.isEmpty()) {
            cityName = "Київ"
        }


        if (advancedSearchClubDto.isAdvanced) {
            println("ADVANCED")

            val advancedSearchResponse =
                service.getClubsByAdvancedSearch(
                    name = advancedSearchClubDto.name,
                    cityName = advancedSearchClubDto.cityName,
                    districtName = advancedSearchClubDto.districtName,
                    stationName = advancedSearchClubDto.stationName,
                    sort = advancedSearchClubDto.sort,
                    categoriesName = advancedSearchClubDto.categoriesName,
                    isCenter = advancedSearchClubDto.isCenter,
                    page = page

                )
            //  println(advancedSearchResponse.body()?.content.toString())
            if (advancedSearchResponse.isSuccessful) {

                val searchedClubs =
                    checkNotNull(advancedSearchResponse.body()).content.map { it.toClub() }
                //println(advancedSearchResponse.body()?.content?.get(0)?.categories)
//                if (searchedClubs.isEmpty()){
//                    return LoadResult.Error(HttpException(advancedSearchResponse))
//                }
                println("content " + searchedClubs.toString())
                println(page)
                val nextKey = if (searchedClubs.size < (pageSize) - 2) null else page + 1
                val prevKey = if (page == 0) null else page - 1
                return LoadResult.Page(searchedClubs, prevKey, nextKey)
            } else {
                println(advancedSearchResponse.errorBody())
                return LoadResult.Error(HttpException(advancedSearchResponse))
            }


        } else {

            println("DEFAULT")

            val searchResponse =
                service.getAllClubs(clubName = clubName,
                    cityName = cityName,
                    isOnline,
                    categoryName,
                    page = page)



            if (searchResponse.isSuccessful) {
                val clubs = checkNotNull(searchResponse.body()).content.map { it.toClub() }
                println(searchResponse.body()!!?.content[0].categories)
                println(page)
                val nextKey = if (clubs.size < pageSize) null else page + 1
                val prevKey = if (page == 0) null else page - 1
                return LoadResult.Page(clubs, prevKey, nextKey)
            } else {
                println(searchResponse.errorBody().toString())
                return LoadResult.Error(HttpException(searchResponse))
            }

        }
    }

    override fun getRefreshKey(state: PagingState<Int, ClubModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }


}