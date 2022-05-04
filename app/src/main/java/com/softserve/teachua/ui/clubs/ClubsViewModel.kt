package com.softserve.teachua.ui.clubs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.softserve.teachua.dto.AdvancedSearchClubDto
import com.softserve.teachua.dto.SearchClubDto
import com.softserve.teachua.retrofit.Common
import com.softserve.teachua.service.ClubsPageSource

class ClubsViewModel(
    private var searchClubDto: SearchClubDto,
    private var advancedSearchClubDto: AdvancedSearchClubDto,
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }

    val text: LiveData<String> = _text

    val clubs = Pager(config = PagingConfig(pageSize = 2), pagingSourceFactory = {
        ClubsPageSource(Common.retrofitService, searchClubDto, advancedSearchClubDto)
    }).flow.cachedIn(viewModelScope)
}