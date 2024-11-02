package com.uniandes.vynilos

import android.app.Application
import com.uniandes.vynilos.di.artistModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application()  {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(artistModule)
        }
    }
}