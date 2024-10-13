package com.uniandes.vynilos.domain.model

import com.uniandes.vynilos.remote.entity.UserResponse

data class User(
    val name: String,
)


fun UserResponse.toDomain() = User(
    name,
)