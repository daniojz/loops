package com.example.loops.fragments.cloudSongList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.loops.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class cloudSongListViewModel : ViewModel() {

    fun getDeviceSongs(): MutableLiveData<List<Song>> {
        val songs = MutableLiveData<List<Song>>()
        GlobalScope.launch(Dispatchers.Main) {

        }
        return songs
    }


}