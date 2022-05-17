package com.softserve.teachua.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.data.dto.UserDto
import com.softserve.teachua.service.CurrentUserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val currentUserService: CurrentUserService,
) :
    ViewModel() {

    private var _user: MutableStateFlow<Resource<UserDto>> =
        MutableStateFlow(Resource.loading())


    val user: StateFlow<Resource<UserDto>>
        get() = _user


    //Delay is made
    fun loadUser() =
        viewModelScope.launch {
            delay(1)
            _user.value = Resource.loading()
            _user.value = currentUserService.getUser()

        }


    fun logOut() {
        viewModelScope.launch {
            _user.value = Resource.error()
            currentUserService.clearCurrentUser()
            currentUserService.clearUser()
        }
    }

}