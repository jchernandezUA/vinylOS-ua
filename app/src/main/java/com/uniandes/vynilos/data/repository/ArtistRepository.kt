package com.uniandes.vynilos.data.repository

import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.common.resultOrError
import com.uniandes.vynilos.data.model.Artist
import com.uniandes.vynilos.data.model.DTO
import com.uniandes.vynilos.data.remote.service.ArtistServiceAdapter

interface ArtistRepository {
    suspend fun getArtists(): DataState<List<Artist>>
    suspend fun getArtist(id: Int): DataState<Artist>
}

class ArtistRepositoryImpl(
    private val artistService: ArtistServiceAdapter
) : ArtistRepository {

    override suspend fun getArtists(): DataState<List<Artist>> {
        return resultOrError {
            val artistResponse = artistService.getArtist()
            artistResponse.DTO()
        }
    }

    override suspend fun getArtist(id: Int): DataState<Artist> {
        return resultOrError {
            val artistResponse = artistService.getArtistById(id)
            artistResponse.DTO()
        }
    }
}