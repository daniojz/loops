package com.example.loops.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.loops.R
import com.example.loops.model.PlayerControl
import com.example.loops.model.Song
import com.example.loops.services.MusicPlayerService
import com.example.loops.viewModel.SongViewModel
import com.google.android.material.slider.Slider
import java.io.Serializable

class SongActivity : AppCompatActivity(), PlayerControl, View.OnClickListener{

    private lateinit var songViewModel: SongViewModel

    private lateinit var selectedSong: Song

    private lateinit var mService: MusicPlayerService
    private var bindState: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)

        init()
    }

    private val connection = object :
            ServiceConnection { //conexion que se usa para conectarse al servicio de MusicPlayer

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

    private fun initMusicPlayerService() {
        Intent(this, MusicPlayerService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun init() {
        songViewModel = ViewModelProvider(this).get(SongViewModel::class.java)
        selectedSong = intent.getSerializableExtra("song") as Song

        showData()
        initMusicPlayerService()
    }

    private fun showData(){
        this.findViewById<TextView>(R.id.title_song_songActivity).also {
            it.text = selectedSong.title
            it.isSelected = true
        }
        this.findViewById<TextView>(R.id.artist_name_songActivity).text = selectedSong.artistName
        this.findViewById<Slider>(R.id.slider_song_progress).valueTo = selectedSong.duration

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

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }
}