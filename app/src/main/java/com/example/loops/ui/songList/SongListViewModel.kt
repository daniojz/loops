package com.example.loops.ui.songList

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.example.loops.adapter.CustomAdapterSongList
import com.example.loops.model.Song
import com.example.loops.util.LocalSongsProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SongListViewModel() : ViewModel() {

    private var listSongs = MutableLiveData<List<Song>>()

    fun showAllDeviceSongs(adapter:CustomAdapterSongList, view: View) {
        GlobalScope.launch(Dispatchers.Main) {
            getAllDeviceSongs(view)
            listSongs.value?.let { adapter.setSongs(it) }
        }
    }

    fun getListSongs(): MutableLiveData<List<Song>> {
        return listSongs
    }

    private fun getAllDeviceSongs(view: View) {
        listSongs.value = LocalSongsProvider.getAllDeviceSongs(view.context)
    }

    fun clickSong(position:Int): Song? {
        return listSongs.value?.get(position)?.let { it }
    }



}