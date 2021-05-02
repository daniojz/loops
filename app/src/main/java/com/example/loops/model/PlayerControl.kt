package com.example.loops.model

import java.net.URI

interface PlayerControl {
    fun playSong(pathSong: String)
    fun pauseSong()
    fun stopSong()
    fun resumeSong()
    fun nextSong()
    fun previusSong()
}