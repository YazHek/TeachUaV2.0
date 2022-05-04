package com.softserve.teachua.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denzcoskun.imageslider.models.SlideModel
import com.softserve.teachua.model.BannerModel
import com.softserve.teachua.model.CategoryModel
import com.softserve.teachua.service.BannersService
import com.softserve.teachua.service.CategoriesService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "https://speak-ukrainian.org.ua/dev/"
    }
    val baseUrl: LiveData<String> = _text

    private var _banners = MutableStateFlow<List<BannerModel>>(emptyList())

    val banners: StateFlow<List<BannerModel>>
        get() = _banners

    private var _bans = MutableStateFlow<List<SlideModel>>(emptyList())

    val bans: StateFlow<List<SlideModel>>
        get() = _bans

    private var _categories = MutableStateFlow<List<CategoryModel>>(emptyList())

    val categories: StateFlow<List<CategoryModel>>
        get() = _categories

    private val _staticMainImg = MutableLiveData<String>().apply {
        value = baseUrl.value + "static/images/about/challenge_2.png"
    }
    val staticMainImg: LiveData<String>
        get() = _staticMainImg

    fun loadBanners() = viewModelScope.launch { _banners.value = BannersService().getAllBanners() }

    fun loadCategories() =
        viewModelScope.launch { _categories.value = CategoriesService().getAllCategories() }


    fun loadBans() = viewModelScope.launch {
        _banners.value = BannersService().getAllBanners()
        for (ban in _banners.value) {

            _bans.value.apply {
                SlideModel("https://speak-ukrainian.org.ua/dev/" + ban.bannerPicture,
                    ban.bannerTitle)
            }
        }
    }
}