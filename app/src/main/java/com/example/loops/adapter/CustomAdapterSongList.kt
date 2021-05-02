package com.example.loops.adapter

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.loops.R
import com.example.loops.model.Song
import com.squareup.picasso.*
import java.io.File


class CustomAdapterSongList(
        val context: Context,
        val lifecycle: LifecycleOwner,
        val layout: Int,
        onSongListener: OnSongListener
) : RecyclerView.Adapter<CustomAdapterSongList.ViewHolder>() {

    private var ListSongs: List<Song> = emptyList()
    private var onSongListener = onSongListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewlayout = layoutInflater.inflate(layout, parent, false)
        return ViewHolder(viewlayout, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsong = ListSongs[position]
        holder.bind(itemsong, lifecycle, onSongListener)
    }

    override fun getItemCount(): Int {
        return ListSongs.size
    }

    internal fun setSongs(songs: List<Song>) {
        this.ListSongs = songs
        notifyDataSetChanged()
    }

    class ViewHolder(viewlayout: View, val context: Context) : RecyclerView.ViewHolder(viewlayout), View.OnClickListener {

        private lateinit var onSongListener:OnSongListener

        fun bind(dataitem: Song, lifecycle: LifecycleOwner, onSongListener: OnSongListener) {
            // itemview es el item de diseño
            // al que hay que poner los datos del objeto dataItem
            itemView.findViewById<TextView>(R.id.title_song).text = dataitem.title
            itemView.findViewById<TextView>(R.id.artist_name).text = dataitem.artistName
            this.onSongListener = onSongListener

            var song_artWork = Uri.parse("content://media/external/audio/albumart")
            song_artWork = ContentUris.withAppendedId(song_artWork, dataitem.id)

            Picasso.get()
                    .load(File(song_artWork.getPath()))
                    .error(R.drawable.ic_standard_image_song)
                    .into(itemView.findViewById<ImageView>(R.id.iv_image_song))

            itemView.tag = dataitem
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            onSongListener.onSongClick(adapterPosition, itemView)
        }
    }

    interface OnSongListener {
        fun onSongClick(position: Int, itemView: View)
    }
}
