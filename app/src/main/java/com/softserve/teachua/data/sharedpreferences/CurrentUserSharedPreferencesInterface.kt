package com.softserve.teachua.data.sharedpreferences

import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.data.dto.CurrentUserDto

interface CurrentUserSharedPreferencesInterface {

    fun getCurrentUser() : Resource<CurrentUserDto>

    fun setCurrentUser(currentUserDto: CurrentUserDto)

    fun clearCurrentUser()

}