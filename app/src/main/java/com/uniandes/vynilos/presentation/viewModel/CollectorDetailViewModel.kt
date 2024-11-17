package com.uniandes.vynilos.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Collector
import com.uniandes.vynilos.data.repository.CollectorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CollectorDetailViewModel(
    private val collectorId: Int,
    private val collectorRepository: CollectorRepository
) : ViewModel() {

    private val _collectorResult = MutableStateFlow<DataState<Collector>>(DataState.Idle)
    val collectorResult: StateFlow<DataState<Collector>> = _collectorResult

    init {
        getCollector()
    }

    private fun getCollector() {
        viewModelScope.launch {
            _collectorResult.value = DataState.Loading
            _collectorResult.value = collectorRepository.getCollector(collectorId)
        }
    }
}
