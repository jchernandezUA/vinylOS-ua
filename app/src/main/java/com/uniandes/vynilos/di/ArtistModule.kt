package com.uniandes.vynilos.di

import com.uniandes.vynilos.common.NetworkModule.createService
import com.uniandes.vynilos.data.remote.service.ArtistServiceAdapter
import com.uniandes.vynilos.data.repository.ArtistRepository
import com.uniandes.vynilos.data.repository.ArtistRepositoryImpl
import com.uniandes.vynilos.presentation.viewModel.ListArtistViewModel
import org.koin.dsl.module


val artistModule = module {
    single { createService(ArtistServiceAdapter::class.java) }
    factory<ArtistRepository> { ArtistRepositoryImpl(get()) }
    factory { ListArtistViewModel(get()) }
}