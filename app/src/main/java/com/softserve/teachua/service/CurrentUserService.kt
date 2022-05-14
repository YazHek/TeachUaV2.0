package com.softserve.teachua.service

import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.data.dto.CurrentUserDto
import com.softserve.teachua.data.sharedpreferences.CurrentUserSharedPreferencesInterface
import kotlinx.coroutines.delay

class CurrentUserService(
    private val userSource : CurrentUserSharedPreferencesInterface
)
{
    fun clearCurrentUser(){
        userSource.clearCurrentUser()
    }

    fun getCurrentUser() : Resource<CurrentUserDto>{
        return userSource.getCurrentUser()
    }

    fun setCurrentUser(currentUserDto: CurrentUserDto){
        userSource.setCurrentUser(currentUserDto)
    }

}