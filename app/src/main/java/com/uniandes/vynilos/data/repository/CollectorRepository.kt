package com.uniandes.vynilos.data.repository

import android.util.Log
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.common.resultOrError
import com.uniandes.vynilos.data.model.Collector
import com.uniandes.vynilos.data.model.CollectorAlbum
import com.uniandes.vynilos.data.model.toModel
import com.uniandes.vynilos.data.model.toModelList
import com.uniandes.vynilos.data.remote.service.CollectorServiceAdapter

interface CollectorRepository {
    suspend fun getCollectors(): DataState<List<Collector>>
    suspend fun getCollector(id: Int): DataState<Collector>
    suspend fun getCollectorAlbum(id: Int) : DataState<List<CollectorAlbum>>
}

class CollectorRepositoryImpl(
    private val collectorService: CollectorServiceAdapter
) : CollectorRepository {

    override suspend fun getCollectors(): DataState<List<Collector>> {
        return resultOrError {
            collectorService.getCollectors().toModelList()
        }
    }

    override suspend fun getCollector(id: Int): DataState<Collector> {
        return resultOrError {
            collectorService.getCollectorById(id).toModel()
        }
    }

    override suspend fun getCollectorAlbum(id: Int): DataState<List<CollectorAlbum>>{
        return resultOrError {
           collectorService.getCollectorAlbums(id).toModelList()
        }
    }
}
