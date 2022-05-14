package com.softserve.teachua.service

import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.data.dto.CurrentUserDto
import com.softserve.teachua.data.sharedpreferences.CurrentUserSharedPreferencesInterface

class CurrentUserService(
    private val userSource : CurrentUserSharedPreferencesInterface
)
{

    fun getCurrentUser() : Resource<CurrentUserDto>{
        return userSource.getCurrentUser()
    }

    fun setCurrentUser(currentUserDto: CurrentUserDto){
        userSource.setCurrentUser(currentUserDto)
    }

}