package com.softserve.teachua.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denzcoskun.imageslider.models.SlideModel
import com.softserve.teachua.app.baseImageUrl
import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.data.model.BannerModel
import com.softserve.teachua.data.model.CategoryModel
import com.softserve.teachua.service.BannersService
import com.softserve.teachua.service.CategoriesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val categoriesService: CategoriesService,
    private val bannersService: BannersService,
) : ViewModel() {

    //might be some problems due to implementing "by lazy". It changes only once.
    val bansList : ArrayList<SlideModel> by lazy { loadBansList() }

    private var _banners = MutableStateFlow<Resource<List<BannerModel>>>(Resource.loading())

    val banners: StateFlow<Resource<List<BannerModel>>>
        get() = _banners

    private var _categories = MutableStateFlow<Resource<List<CategoryModel>>>(Resource.loading())

    val categories: StateFlow<Resource<List<CategoryModel>>>
        get() = _categories


    val staticMainImg: String
        get() = baseImageUrl + "static/images/about/challenge_2.png"

    fun loadData() = viewModelScope.launch {
        loadBanners()
        loadCategories()
    }

    private fun loadBansList(): ArrayList<SlideModel> {
        val bansList = ArrayList<SlideModel>()
        banners.value.data?.let {
            for (ban in it) {
                bansList.add(
                    SlideModel(
                        baseImageUrl + ban.bannerPicture,
                        ban.bannerTitle + "\n\n" + ban.bannerSubtitle,
                    )
                )
            }
        }
        return bansList
    }

    private suspend fun loadBanners() {
        _banners.value = bannersService.getAllBanners()
    }

    private suspend fun loadCategories() {
        _categories.value = categoriesService.getAllCategories()
    }

}