package com.softserve.teachua.ui.clubs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.data.dto.AdvancedSearchClubDto
import com.softserve.teachua.data.dto.SearchClubDto
import com.softserve.teachua.data.model.CityModel
import com.softserve.teachua.data.model.DistrictModel
import com.softserve.teachua.data.model.StationModel
import com.softserve.teachua.data.retrofit.Common
import com.softserve.teachua.service.CitiesService
import com.softserve.teachua.service.ClubsPageSource
import com.softserve.teachua.service.DistrictService
import com.softserve.teachua.service.StationsService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClubsViewModel @Inject constructor(
    private val districtService: DistrictService,
    private val stationsService: StationsService,
    private val citiesService: CitiesService,

    ) : ViewModel() {

    private var _searchClubDto =
        MutableLiveData<SearchClubDto>(SearchClubDto("", "", false, "", 0))
    private var _advancedSearchClubDto = MutableLiveData<AdvancedSearchClubDto>(
        AdvancedSearchClubDto("",
            null,
            "",
            "",
            "",
            "name,asc",
            0,
            emptyList(),
            isOnline = false,
            isCenter = false,
            isAdvanced = false))

    val searchClubDto: LiveData<SearchClubDto>
        get() = _searchClubDto

    val advancedSearchClubDto: LiveData<AdvancedSearchClubDto>
        get() = _advancedSearchClubDto

    private var _cities = MutableStateFlow<Resource<List<CityModel>>>(Resource.loading())

    val cities: StateFlow<Resource<List<CityModel>>>
        get() = _cities

    private var _districts = MutableStateFlow<Resource<List<DistrictModel>>>(
        Resource.loading())

    val districts: StateFlow<Resource<List<DistrictModel>>>
        get() = _districts

    private var _stations = MutableStateFlow<Resource<List<StationModel>>>(Resource.loading())

    val stations: StateFlow<Resource<List<StationModel>>>
        get() = _stations

    fun loadCities() =
        viewModelScope.launch { _cities.value = citiesService.getAllCities() }

    fun loadDistricts(cityName: String) =
        viewModelScope.launch {
            println("lodaded " + cityName)
            _districts.value = Resource.loading()
            _districts.value = districtService.getAllDistricts(cityName)
            println("disrIIcts " + districts.value.data)
//            println("new " + _districts.value.data)
        }

    fun loadStations(cityName: String) =
        viewModelScope.launch {
            _stations.value = Resource.loading()
            _stations.value = stationsService.getAllStations(cityName)
        }

    fun loadClubs(cityName: String) =
        viewModelScope.launch { _stations.value = stationsService.getAllStations(cityName) }

    val clubs = Pager(config = PagingConfig(pageSize = 2), pagingSourceFactory = {
        ClubsPageSource(Common.retrofitService,
            searchClubDto.value!!,
            advancedSearchClubDto.value!!)
    }).flow.cachedIn(viewModelScope)
}