package com.uniandes.vynilos.data.repository

import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.common.resultOrError
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.model.Artist
import com.uniandes.vynilos.data.model.DTO
import com.uniandes.vynilos.data.remote.service.ArtistServiceAdapter

interface ArtistRepository {
    suspend fun getArtists(): DataState<List<Artist>>
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
}