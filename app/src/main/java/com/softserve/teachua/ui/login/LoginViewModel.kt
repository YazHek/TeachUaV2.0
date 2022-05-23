package com.softserve.teachua.ui.login

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.data.dto.*
import com.softserve.teachua.service.CurrentUserService
import com.softserve.teachua.service.LoggingService
import com.softserve.teachua.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loggingService: LoggingService,
    private val userService: UserService,
    private val currentUserService: CurrentUserService,
) : ViewModel() {


    private var _user: MutableStateFlow<Resource<UserDto>> =
        MutableStateFlow(Resource.loading())


    val user: StateFlow<Resource<UserDto>>
        get() = _user

    private var _loggedDto =
        MutableStateFlow<Resource<UserLoggedDto>>(Resource.loading())


    val loggedDto: StateFlow<Resource<UserLoggedDto>>
        get() = _loggedDto


    fun load(userLoginDto: UserLoginDto) = viewModelScope.launch {
        _loggedDto.value = Resource.loading()
        _loggedDto.value = loggingService.getLoggedUser(userLoginDto)

        if (loggedDto.value.status == Resource.Status.SUCCESS) {
            val currentUserDto = CurrentUserDto(
                id = loggedDto.value.data?.id!!,
                email = loggedDto.value.data?.email!!,
                token = "Bearer " + loggedDto.value.data?.accessToken!!
            )
            currentUserService.setCurrentUser(currentUserDto)

            val userCredentials = UserLoginDto(
                email = userLoginDto.email,
                password = userLoginDto.password
            )

            currentUserService.setUserCredentials(userCredentials)


        }

    }


    fun loadUser(token: String, id: Int) = viewModelScope.launch {
        _user.value = Resource.loading()
        _user.value = userService.getUserById(token, id)

        if (user.value.status == Resource.Status.SUCCESS) {
            val userDto = UserDto(
                id = user.value.data?.id!!,
                firstName = user.value.data?.firstName!!,
                lastName = user.value.data?.lastName!!,
                phone = user.value.data?.phone!!,
                email = user.value.data?.email!!,
                password = user.value.data?.password!!,
                roleName = user.value.data?.roleName!!,
                urlLogo = user.value.data?.urlLogo!!,
                status = user.value.data?.status!!,
                logInTime = System.currentTimeMillis()
            )
            currentUserService.setUser(userDto)
        }
    }

}