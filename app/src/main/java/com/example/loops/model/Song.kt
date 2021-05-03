package com.example.loops.model

import android.net.Uri
import java.io.Serializable

data class Song(
        val id: Long,
        val contentUri: String,
        val title: String,
        val trackNumber: Int,
        val year: Int,
        val duration: Float,
        val albumName: String,
        val artistId: Int,
        val albumId: Long,
        val artistName: String): Serializable
