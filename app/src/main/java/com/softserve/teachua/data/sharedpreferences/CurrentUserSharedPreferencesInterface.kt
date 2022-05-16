package com.softserve.teachua.data.sharedpreferences

import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.data.dto.CurrentUserDto
import com.softserve.teachua.data.dto.UserDto

interface CurrentUserSharedPreferencesInterface {

    fun getCurrentUser() : Resource<CurrentUserDto>

    fun setCurrentUser(currentUserDto: CurrentUserDto)

    fun clearCurrentUser()

    fun getUser() : Resource<UserDto>

    fun setUser(userDto: UserDto)

    fun clearUser()

}