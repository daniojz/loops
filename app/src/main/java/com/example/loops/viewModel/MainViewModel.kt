package com.example.loops.viewModel

import androidx.lifecycle.ViewModel
import com.example.loops.model.PlayerControl
import com.example.loops.services.MusicPlayerService

class MainViewModel() : ViewModel(), PlayerControl {

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