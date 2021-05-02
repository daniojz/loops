package com.example.loops.ui.albums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.loops.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AlbumsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    fun getDeviceSongs(): MutableLiveData<List<Song>> {
        val songs = MutableLiveData<List<Song>>()
        GlobalScope.launch(Dispatchers.Main) {

        }
        return songs
    }


}