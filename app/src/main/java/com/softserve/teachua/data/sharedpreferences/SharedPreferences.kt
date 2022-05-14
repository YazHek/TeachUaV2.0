package com.softserve.teachua.data.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.data.dto.CurrentUserDto

class SharedPreferences(
    val context : Context
)
    : CurrentUserSharedPreferencesInterface {

    private val settingName : String = "Settings"
    private val sharedPreferences = context.getSharedPreferences(settingName, Context.MODE_PRIVATE)


    override fun getCurrentUser(): Resource<CurrentUserDto> {
        val id = sharedPreferences.getInt("CurrentUserId", 0)
        val email = sharedPreferences.getString("CurrentUserEmail", "")
        val token = sharedPreferences.getString("CurrentUserToken", "")
        if (token.equals("")){
            return Resource.error()
        }
        val currentUserDto = CurrentUserDto(id, email.toString(), token.toString())
        return Resource.success(currentUserDto)
    }

    override fun setCurrentUser(currentUserDto: CurrentUserDto) = sharedPreferences.edit()
        .putInt("CurrentUserId", currentUserDto.id)
        .putString("CurrentUserEmail", currentUserDto.email)
        .putString("CurrentUserToken", currentUserDto.token)
        .apply()
}