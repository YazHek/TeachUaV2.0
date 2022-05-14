package com.softserve.teachua.data.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.data.dto.CurrentUserDto

class SharedPreferences(
    val context : Context
)
    : CurrentUserSharedPreferencesInterface {

    private val userCredentials = context.getSharedPreferences("userCredentials", Context.MODE_PRIVATE)

    override fun getCurrentUser(): Resource<CurrentUserDto> {
        val id = userCredentials.getInt("CurrentUserId", 0)
        val email = userCredentials.getString("CurrentUserEmail", "")
        val token = userCredentials.getString("CurrentUserToken", "")
        if (token.equals("")){
            return Resource.error()
        }
        val currentUserDto = CurrentUserDto(id, email.toString(), token.toString())
        return Resource.success(currentUserDto)
    }

    override fun setCurrentUser(currentUserDto: CurrentUserDto) = userCredentials.edit()
        .putInt("CurrentUserId", currentUserDto.id)
        .putString("CurrentUserEmail", currentUserDto.email)
        .putString("CurrentUserToken", currentUserDto.token)
        .apply()

    override fun clearCurrentUser() {
        userCredentials.edit().clear().apply()
    }
}