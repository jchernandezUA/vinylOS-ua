package com.uniandes.vynilos.data.repository

import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.User
import com.uniandes.vynilos.data.remote.service.UserService
import com.uniandes.vynilos.common.resultOrError
import com.uniandes.vynilos.data.model.toDomain

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