package com.softserve.teachua.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private val categoriesService : CategoriesService,
    private val bannersService: BannersService
    ) : ViewModel() {

    private var _banners = MutableStateFlow<Resource<List<BannerModel>>>(Resource.loading())

    val banners: StateFlow<Resource<List<BannerModel>>>
        get() = _banners

//    private var _bans = MutableStateFlow<List<SlideModel>>(emptyList())
//
//    val bans: StateFlow<List<SlideModel>>
//        get() = _bans

    private var _categories = MutableStateFlow<List<CategoryModel>>(emptyList())

    val categories: StateFlow<List<CategoryModel>>
        get() = _categories

    private val _staticMainImg = MutableLiveData<String>().apply {
        value = baseImageUrl + "static/images/about/challenge_2.png"
    }
    val staticMainImg: LiveData<String>
        get() = _staticMainImg


    fun loadBanners() = viewModelScope.launch { _banners.value = bannersService.getAllBanners() }

    fun loadCategories() =
        viewModelScope.launch { _categories.value = categoriesService.getAllCategories() }

}