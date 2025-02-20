package com.sundev.testnotes

import android.app.Application
import android.content.Context

class NotesApp : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object {

        lateinit var appContext: Context
            private set
    }

}