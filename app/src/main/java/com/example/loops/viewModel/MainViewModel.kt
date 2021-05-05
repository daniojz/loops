package com.example.loops.viewModel

import androidx.lifecycle.ViewModel
import com.example.loops.model.PlayerControl
import com.example.loops.services.MusicPlayerService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser




class MainViewModel() : ViewModel(), PlayerControl {

    private lateinit var mService: MusicPlayerService
    private lateinit var firebaseAuth:FirebaseAuth

    fun checkAuth():Boolean{
        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth.getCurrentUser() != null) {
            return true
        }
        return false
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