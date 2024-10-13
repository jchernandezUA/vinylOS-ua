package com.uniandes.vynilos.domain.repository

import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.domain.model.User
import com.uniandes.vynilos.remote.service.UserService
import com.uniandes.vynilos.common.resultOrError
import com.uniandes.vynilos.domain.model.toDomain

interface ExampleRepository {
    suspend fun getGreeting(userId: String): DataState<User>
}
class ExampleRepositoryImpl(
    private val userService: UserService
): ExampleRepository {

    override suspend fun getGreeting(userId: String): DataState<User> {
        return resultOrError {
            userService.getUser(userId).toDomain()
        }
    }

}