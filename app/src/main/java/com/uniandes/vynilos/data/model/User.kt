package com.uniandes.vynilos.data.model

import com.uniandes.vynilos.data.remote.entity.UserResponse

data class User(
    val name: String,
)


fun UserResponse.toDomain() = User(
    name,
)