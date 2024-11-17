package com.uniandes.vynilos.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Collector
import com.uniandes.vynilos.data.repository.CollectorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListCollectorViewModel(
    private val collectorRepository: CollectorRepository
) : ViewModel() {

    private val _collectorsResult = MutableStateFlow<DataState<List<Collector>>>(DataState.Idle)
    val collectorsResult: StateFlow<DataState<List<Collector>>> = _collectorsResult

    fun getCollectors() {
        viewModelScope.launch {
            _collectorsResult.value = DataState.Loading
            _collectorsResult.value = collectorRepository.getCollectors()
        }
    }
}
