package com.uniandes.vynilos.presentation.viewModel

import android.provider.ContactsContract.Data
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Collector
import com.uniandes.vynilos.data.model.CollectorAlbum
import com.uniandes.vynilos.data.repository.CollectorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.lang.Exception

class CollectorDetailViewModel(
    private val collector: Collector,
    private val collectorRepository: CollectorRepository
) : ViewModel() {

    private val _collectorResult = MutableStateFlow<DataState<Collector>>(DataState.Idle)
    val collectorResult: StateFlow<DataState<Collector>> = _collectorResult

    private val _collectorAlbumResult = MutableStateFlow<DataState<List<CollectorAlbum>>>(DataState.Idle)
    val collectorAlbumResult: StateFlow<DataState<List<CollectorAlbum>>> = _collectorAlbumResult

    private val _collectorState = MutableStateFlow<DataState<Pair<Collector,List<CollectorAlbum>>>>(DataState.Idle)
    val collectorState = _collectorResult.combine(_collectorAlbumResult){ collector, collectorAlbum ->

        if(collector is DataState.Loading || collectorAlbum is DataState.Loading) {
            DataState.Loading

        }else if(collector is DataState.Success && collectorAlbum is DataState.Success){
            DataState.Success(Pair(collector.data, collectorAlbum.data))

        }else if(collector is DataState.Error || collectorAlbum is DataState.Error){
            if(collector is DataState.Error){
                DataState.Error(collector.error.copy(collector.error.message))
            }else{
                DataState.Error((collectorAlbum as DataState.Error).error.copy(collectorAlbum.error.message))
            }

        } else {

        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly,DataState.Idle)




    fun getCollector() {
        viewModelScope.launch {
            _collectorResult.value = DataState.Loading
            _collectorAlbumResult.value = DataState.Loading
            _collectorResult.value = collectorRepository.getCollector(collector.id)
            _collectorAlbumResult.value = collectorRepository.getCollectorAlbum(collector.id)
        }
    }


}
