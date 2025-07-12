package com.philexliveprojects.ordeist

import android.app.Application
import com.philexliveprojects.ordeist.data.AppContainer

class OrdeistApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}