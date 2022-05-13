package com.softserve.teachua.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.data.dto.SearchClubDto
import com.softserve.teachua.data.dto.UserLoggedDto
import com.softserve.teachua.data.dto.UserLoginDto
import com.softserve.teachua.service.LoggingService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loggingService: LoggingService) : ViewModel() {

    private var _searchClubDto =
        MutableLiveData<SearchClubDto>(SearchClubDto("", "", false, "", 0))

    private var _loggedDto =
        MutableStateFlow<Resource<UserLoggedDto>>(Resource.loading())


    val loggedDto: StateFlow<Resource<UserLoggedDto>>
        get() = _loggedDto


    fun load(userLoginDto: UserLoginDto) = viewModelScope.launch {
        _loggedDto.value = Resource.loading()
        _loggedDto.value = loggingService.getLoggedUser(userLoginDto)
    }

}