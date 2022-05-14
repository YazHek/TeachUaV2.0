package com.softserve.teachua.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.data.dto.CurrentUserDto
import com.softserve.teachua.service.CurrentUserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val currentUserService: CurrentUserService) : ViewModel() {

    private var _userToken : MutableStateFlow<Resource<CurrentUserDto>> = MutableStateFlow(Resource.loading())

    val userToken : StateFlow<Resource<CurrentUserDto>>
        get() = _userToken

    fun loadData() =
        viewModelScope.launch {
            _userToken.value = currentUserService.getCurrentUser()
        }

    fun logOut() {
        viewModelScope.launch {
            _userToken.value = Resource.error()
            currentUserService.clearCurrentUser()
        }
    }

}