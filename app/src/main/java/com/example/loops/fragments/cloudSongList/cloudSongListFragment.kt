package com.example.loops.fragments.cloudSongList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.loops.R

class cloudSongListFragment : Fragment() {

    private lateinit var notificationsViewModel: cloudSongListViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProvider(this).get(cloudSongListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_cloudSongList, container, false)
        return root
    }
}