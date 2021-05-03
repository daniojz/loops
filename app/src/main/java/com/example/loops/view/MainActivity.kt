package com.example.loops.view

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.drawable.Drawable
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
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.loops.R
import com.example.loops.model.PlayerControl
import com.example.loops.services.MusicPlayerService
import com.example.loops.ui.albums.AlbumsFragment
import com.example.loops.ui.home.HomeFragment
import com.example.loops.ui.songList.SongListFragment
import com.example.loops.viewModel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.loops.model.Song


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, PlayerControl, View.OnClickListener {

    private lateinit var mainViewModel: MainViewModel

    private lateinit var homeFragment: HomeFragment
    private lateinit var songListFragment: SongListFragment
    private lateinit var albumsFragment: AlbumsFragment
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
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            bindState = false
        }
    }

    val readPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        init()

        readPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun init() {
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setNavController()
        initMusicPlayerService()

        songListFragment.getClickedSong().observe(this, Observer { it ->
            it?.let {
                this.selectedSong = it
                showControlMusicCard(true, it)
                playSong(it.contentUri)
            }
        })

        setRandomBackground()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        lateinit var selectedFragment: Fragment

        when (item.itemId) {
            R.id.navigation_home -> selectedFragment = homeFragment
            R.id.navigation_songsList -> selectedFragment = songListFragment
            R.id.navigation_albums -> selectedFragment = albumsFragment
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
        albumsFragment = AlbumsFragment()

        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, homeFragment)
            .commit()

    }

    private fun initMusicPlayerService() {
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
            if (!mService.isPlaying()) {
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

    override fun playSong(pathSong: String) {
        mService.playSong(pathSong)
    }

    override fun pauseSong() {
        mService.pauseSong()
    }

    override fun stopSong() {
        mService.stopSong()
    }

    override fun resumeSong() {
        if (!mService.isPlaying()) {
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

