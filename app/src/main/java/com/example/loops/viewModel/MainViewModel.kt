package com.example.loops.viewModel

import android.app.Application
import android.content.Context
import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.loops.model.BindMusicPlayerService
import com.example.loops.model.PlayerControl
import com.example.loops.model.Song
import com.example.loops.services.MusicPlayerService
import com.example.loops.util.LocalSongsProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.ArrayList

class MainViewModel() : ViewModel(), PlayerControl {

    private lateinit var bindMusicPlayerService: BindMusicPlayerService
    private lateinit var mService: MusicPlayerService

    fun set(){

    }

    override fun playSong(pathSong: String) {
        TODO("Not yet implemented")
    }

    override fun pauseSong() {
        TODO("Not yet implemented")
    }

    override fun stopSong() {
        TODO("Not yet implemented")
    }

    override fun resumeSong() {
        TODO("Not yet implemented")
    }

    override fun nextSong() {
        TODO("Not yet implemented")
    }

    override fun previusSong() {
        TODO("Not yet implemented")
    }

}