package com.softserve.teachua.data.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.data.dto.CurrentUserDto
import com.softserve.teachua.data.dto.UserDto

class SharedPreferences(
    val context: Context,
) : CurrentUserSharedPreferencesInterface {

    private val userCredentials =
        context.getSharedPreferences("userCredentials", Context.MODE_PRIVATE)

    override fun getCurrentUser(): Resource<CurrentUserDto> {
        val id = userCredentials.getInt("CurrentUserId", 0)
        val email = userCredentials.getString("CurrentUserEmail", "")
        val token = userCredentials.getString("CurrentUserToken", "")
        if (token.equals("")) {
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

    override fun getUser(): Resource<UserDto> {
        val id = userCredentials.getInt("userId", 0)
        val firstName = userCredentials.getString("userFirstName", "")
        val lastName = userCredentials.getString("userLastName", "")
        val phone = userCredentials.getString("userPhone", "")
        val email = userCredentials.getString("userEmail", "")
        val password = userCredentials.getString("userPassword", "")
        val roleName = userCredentials.getString("userRoleName", "")
        val urlLogo = userCredentials.getString("userUrlLogo", "")
        val status = userCredentials.getBoolean("userStatus", false)
        if (!status) {
            return Resource.error()
        }
        val userDto = UserDto(
            id,
            firstName.toString(),
            lastName.toString(),
            phone.toString(),
            email.toString(),
            password.toString(),
            roleName.toString(),
            urlLogo.toString(),
            status
        )
        return Resource.success(userDto)
    }

    override fun setUser(userDto: UserDto) = userCredentials.edit()
        .putInt("userId", userDto.id)
        .putString("userFirstName", userDto.firstName)
        .putString("userLastName", userDto.lastName)
        .putString("userPhone", userDto.phone)
        .putString("userEmail", userDto.email)
        .putString("userPassword", userDto.password)
        .putString("userRoleName", userDto.roleName)
        .putString("userUrlLogo", userDto.urlLogo)
        .putBoolean("userStatus", userDto.status)
        .apply()

    override fun clearUser() {
        userCredentials.edit().clear().apply()
    }
}