package com.example.loops.view

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.loops.R
import com.example.loops.fragments.cloudSongList.cloudSongListFragment
import com.example.loops.fragments.home.HomeFragment
import com.example.loops.fragments.songList.SongListFragment
import com.example.loops.model.PlayerControl
import com.example.loops.model.Song
import com.example.loops.services.MusicPlayerService
import com.example.loops.viewModel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, PlayerControl, View.OnClickListener {

    private lateinit var mainViewModel: MainViewModel

    private lateinit var homeFragment: HomeFragment
    private lateinit var songListFragment: SongListFragment
    private lateinit var albumsFragment: cloudSongListFragment
    private lateinit var cardView: CardView

    private lateinit var mService: MusicPlayerService
    private var bindState: Boolean = false

    private lateinit var selectedSong: Song

    private val connection = object :
        ServiceConnection { //conexion que se usa para conectarse al servicio de MusicPlayer

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as MusicPlayerService.LocalBinder
            mService = binder.getService()
            bindState = true

            if (mService.isActive){
                showControlMusicCard(mService.isPlaying, mService.song)
            }
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            bindState = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            val readPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
            readPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Verifica permisos para Android 6.0+
            val permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                Log.d("Mensaje", "No se tiene permiso para leer.")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    225
                )
            } else {
                Log.d("Mensaje", "Se tiene permiso para leer!")
            }
        }
    }

    private fun welcomeInit(){
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun init() {
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        if (!mainViewModel.checkAuth()){
            welcomeInit()
        } else {
            conectMusicPlayerService()
            setNavController()

            songListFragment.getClickedSong().observe(this, Observer { it ->
                it?.let {
                    this.selectedSong = it
                    showControlMusicCard(true, it)
                    playSong(it)
                }
            })

            setRandomBackground()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        lateinit var selectedFragment: Fragment

        when (item.itemId) {
            R.id.navigation_home -> selectedFragment = homeFragment
            R.id.navigation_songsList -> selectedFragment = songListFragment
            R.id.navigation_cloud_music -> selectedFragment = albumsFragment
        }

        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, selectedFragment)
            .commit()

        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Activity", "OnDestroy activity...")

        Intent(this, MusicPlayerService::class.java).also {
            stopService(it)
        }

    }

    private fun setNavController() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(this)

        homeFragment = HomeFragment()
        songListFragment = SongListFragment()
        albumsFragment = cloudSongListFragment()

        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, homeFragment)
            .commit()

    }

    private fun conectMusicPlayerService() {
//        Intent(this, MusicPlayerService::class.java).also {
//            startService(it)
//        }

        Intent(this, MusicPlayerService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

    }

    private fun showControlMusicCard(state: Boolean, song: Song) {
        cardView = findViewById<CardView>(R.id.cardControl)
        val animation: Animation

        cardView.setOnClickListener(this)
        cardView.findViewById<TextView>(R.id.control_artist_name).also {
            it.isSelected = true
            it.text = song.artistName
        }
        cardView.findViewById<TextView>(R.id.control_title_song).also {
            it.isSelected = true
            it.text = song.title
        }
        cardView.findViewById<ImageButton>(R.id.ib_play_pause).also {
            it.setOnClickListener(this)
            if (!mService.isPlaying) {
                it.setImageResource(R.drawable.ic_pause_icon)
            }
        }
        cardView.findViewById<ImageButton>(R.id.ib_previous).setOnClickListener(this)
        cardView.findViewById<ImageButton>(R.id.ib_next).setOnClickListener(this)

        if (state) {
            animation = AnimationUtils.loadAnimation(this, R.anim.anim_show)
            cardView.isGone = false
            cardView.isVisible = true
            cardView.startAnimation(animation)
        } else {
            animation = AnimationUtils.loadAnimation(this, R.anim.anim_hide)
            cardView.isGone = true
            cardView.startAnimation(animation)
        }


    }

    override fun playSong(song: Song) {
        mService.playSong(song)
    }

    override fun pauseSong() {
        mService.pauseSong()
    }

    override fun stopSong() {
        mService.stopSong()
    }

    override fun resumeSong() {
        if (!mService.isPlaying) {
            mService.resumeSong()
            cardView.findViewById<ImageButton>(R.id.ib_play_pause)
                .setImageResource(R.drawable.ic_pause_icon)
        } else {
            mService.pauseSong()
            cardView.findViewById<ImageButton>(R.id.ib_play_pause)
                .setImageResource(R.drawable.ic_play_icon)
        }
    }

    override fun nextSong() {
    }

    override fun previusSong() {
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.ib_play_pause -> resumeSong()
                R.id.ib_next -> nextSong()
                R.id.ib_previous -> previusSong()
                R.id.cardControl -> showSongFragment()
            }
        }
    }

    private fun setRandomBackground() {
        val randomNumber = (Math.random() * 5 + 1).toInt()
        Log.d("id", "bg_main_activity_$randomNumber")
        val background: Drawable = ContextCompat.getDrawable(
            this,
            resources.getIdentifier("bg_main_activity_$randomNumber", "drawable", this.packageName)
        )!!

        this.findViewById<ConstraintLayout>(R.id.activity_container).background = background
    }

    private fun showSongFragment() {
        val intent = Intent(this, SongActivity::class.java)
        intent.putExtra("song", selectedSong)
        startActivity(intent)
    }

}

