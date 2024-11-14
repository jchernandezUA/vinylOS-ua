package com.uniandes.vynilos.di

import com.uniandes.vynilos.common.NetworkModule.createService
import com.uniandes.vynilos.data.remote.service.AlbumServiceAdapter
import com.uniandes.vynilos.data.repository.AlbumRepository
import com.uniandes.vynilos.data.repository.AlbumRepositoryImpl
import com.uniandes.vynilos.presentation.viewModel.ListAlbumViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val albumModule = module {
    single { createService(AlbumServiceAdapter::class.java) }
    single<AlbumRepository> { AlbumRepositoryImpl(get()) }
    viewModel { ListAlbumViewModel(get()) }
}
