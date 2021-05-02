package com.example.loops.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.loops.model.PlayerControl
import com.example.loops.model.Song
import java.net.URI

class MusicPlayerService : Service(), PlayerControl {

    private lateinit var player: MediaPlayer
    private lateinit var song: Song
    private val binder = LocalBinder()

    private var isPlaying = false

    override fun onCreate() {
        super.onCreate()
        Log.d("ServicioMusica", "Servicio onCreate....")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("ServicioMusica", "Servicio onDestroy....")

    }

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    inner class LocalBinder : Binder() { //con esta clase se recoje la propia instancia local de este servicio
        fun getService(): MusicPlayerService = this@MusicPlayerService // para que el "cliente" pueda utilizar sus metodos publicos
    }

    override fun playSong(pathSong: String) {
        if (isPlaying){
            player.stop()
        }
        player = MediaPlayer.create(this, Uri.parse(pathSong))
        player.start()
        isPlaying = true
    }

    override fun pauseSong() {
        player.pause()
        isPlaying = false
    }

    override fun stopSong() {
        player.stop()
        isPlaying = false
    }

    override fun resumeSong() {
        player.start()
        isPlaying = true
    }

    override fun nextSong() {
        TODO("Not yet implemented")
    }

    override fun previusSong() {
        TODO("Not yet implemented")
    }

    fun isPlaying(): Boolean {
        return isPlaying
    }

}