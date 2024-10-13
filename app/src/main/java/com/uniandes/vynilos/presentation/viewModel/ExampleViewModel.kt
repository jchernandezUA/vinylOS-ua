package com.uniandes.vynilos.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.domain.model.User
import com.uniandes.vynilos.domain.repository.ExampleRepositoryImpl
import com.uniandes.vynilos.common.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExampleViewModel: ViewModel() {

    private val exampleRepository = ExampleRepositoryImpl(NetworkModule.userService)
    private val _greeting: MutableStateFlow<DataState<User>?> = MutableStateFlow(null)
    val greeting: StateFlow<DataState<User>?>
        get() = _greeting
    private val _userId: MutableStateFlow<String> = MutableStateFlow("")
    val userId: StateFlow<String>
        get() = _userId


    fun setUserId(userId: String) {
        _userId.value = userId
    }
    fun getUser() {
        viewModelScope.launch {
            _greeting.value = DataState()
            _greeting.value = exampleRepository.getGreeting(_userId.value)
        }
    }


    fun getFullName(): String {
        return _greeting.value!!.data!!.name
    }
}