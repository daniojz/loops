package com.example.loops.util

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.loops.model.Song
import java.util.*

object LocalSongsProvider {

    private val allDeviceSongs = ArrayList<Song>()

    private val projection = arrayOf(
            MediaStore.Audio.AudioColumns._ID,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.AudioColumns.TRACK,
            MediaStore.Audio.AudioColumns.YEAR,
            MediaStore.Audio.AudioColumns.DURATION,
            MediaStore.Audio.AudioColumns.ALBUM,
            MediaStore.Audio.AudioColumns.ALBUM_ID,
            MediaStore.Audio.AudioColumns.ARTIST_ID,
            MediaStore.Audio.AudioColumns.ARTIST
    )

    private const val selection = MediaStore.Audio.Media.IS_MUSIC + " !=0"

    private fun getSongs(cursor: Cursor?): List<Song> {
        allDeviceSongs.clear()
        if (cursor != null) {
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val trackColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK)
            val yearColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val artistIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn)
                val track = cursor.getInt(trackColumn)
                val year = cursor.getInt(yearColumn)
                val duration = cursor.getInt(durationColumn)
                val album = cursor.getString(albumColumn)
                val albumId = cursor.getLong(albumIdColumn)
                val artistId = cursor.getInt(artistIdColumn)
                val artist = cursor.getString(artistColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        id
                )

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                allDeviceSongs.add(Song(id, contentUri, title, track, year, duration, album, artistId, albumId, artist))
            }
        }

        return allDeviceSongs.reversed()
    }


        fun getAllDeviceSongs(context: Context): List<Song> {
            val cursor = BuildSongCursor(context)
            return getSongs(cursor)
        }


        private fun BuildSongCursor(context: Context): Cursor? {
            try {
                return context.contentResolver.query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        projection,
                        selection,
                        null,
                        null)
            } catch (e: SecurityException) {
                return null
            }
        }

    }
