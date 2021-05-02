package com.example.loops.model

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import com.example.loops.services.MusicPlayerService

class BindMusicPlayerService {
    private lateinit var mService: MusicPlayerService
    private var bindState: Boolean = false

    private val connection = object : ServiceConnection { //conexion que se usa para conectarse al servicio de MusicPlayer

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as MusicPlayerService.LocalBinder
            mService = binder.getService()
            bindState = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            bindState = false
        }
    }
    
    fun getConnection(): ServiceConnection {
        return connection
    }

    fun getBindState(): Boolean {
        return bindState
    }

    fun getService(): MusicPlayerService {
        return mService
    }
}