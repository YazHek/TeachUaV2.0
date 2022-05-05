package com.softserve.teachua.data.model

import com.softserve.teachua.data.dto.TaskDto
import com.softserve.teachua.data.dto.UserDto

class ChallengeModel(
    val description: String,
    val id: Int,
    val isActive: Boolean,
    val name: String,
    val picture: String,
    val registrationLink: Any,
    val sortNumber: Int,
    val tasks: List<TaskDto>,
    val title: String,
    val user: UserDto
)
