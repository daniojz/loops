package com.example.loops.model

import android.net.Uri

data class Song(
        val id: Long,
        val contentUri: Uri,
        val title: String,
        val trackNumber: Int,
        val year: Int,
        val duration: Int,
        val albumName: String,
        val artistId: Int,
        val albumId: Long,
        val artistName: String)
