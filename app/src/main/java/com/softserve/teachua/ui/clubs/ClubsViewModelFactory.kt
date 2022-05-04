package com.softserve.teachua.ui.clubs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.softserve.teachua.data.dto.AdvancedSearchClubDto
import com.softserve.teachua.data.dto.SearchClubDto

class ClubsViewModelFactory(
    private var searchClubDto: SearchClubDto,
    private var advancedSearchClubDto: AdvancedSearchClubDto,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return ClubsViewModel(searchClubDto, advancedSearchClubDto) as T

    }
}