package com.example.loops.adapter

import android.content.ContentUris
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.RecyclerView
import com.example.loops.R
import com.example.loops.model.Song
import com.squareup.picasso.*
import java.io.File


class CustomAdapterCloudSongList(
        val context: Context,
        val lifecycle: LifecycleOwner,
        val layoutId: Int,
        private var onSongListener: OnSongListener
) : RecyclerView.Adapter<CustomAdapterCloudSongList.ViewHolder>() {

    private var ListSongs: List<Song> = emptyList()
    private var selectedSongs = mutableSetOf<Int>()
    private var playingSong = -1

    var editMode = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            LayoutInflater.from(parent.context).inflate(layoutId, parent, false), context
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(ListSongs[position], lifecycle, onSongListener)


    override fun getItemCount(): Int {
        return ListSongs.size
    }

    internal fun setSongs(songs: List<Song>) {
        this.ListSongs = songs
        notifyDataSetChanged()
    }


    inner class ViewHolder(viewlayout: View, val context: Context) : RecyclerView.ViewHolder(viewlayout), View.OnClickListener, View.OnLongClickListener {

        private lateinit var onSongListener:OnSongListener
        private var title_song = itemView.findViewById<TextView>(R.id.title_song)
        private var artist_name = itemView.findViewById<TextView>(R.id.artist_name)
        private var song_artwork = itemView.findViewById<ImageView>(R.id.iv_image_song)

        private var PrincipalColor =  ContextCompat.getColor(context, R.color.principal_text_color)
        private var SecondaryColor =  ContextCompat.getColor(context, R.color.secondary_text_color)

        fun bind(dataitem: Song, lifecycle: LifecycleOwner, onSongListener: OnSongListener) {
            title_song.text = dataitem.title
            artist_name.text = dataitem.artistName
            this.onSongListener = onSongListener

            var song_artWork = Uri.parse("content://media/external/audio/albumart")
            song_artWork = ContentUris.withAppendedId(song_artWork, dataitem.id)

            Picasso.get()
                    .load(File(song_artWork.getPath()))
                    .error(R.drawable.ic_standard_image_song)
                    .into(song_artwork)

            itemView.tag = dataitem
            updateViewStyle()
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        private fun updateViewStyle(){
                if (adapterPosition == playingSong) {
                    title_song.setTextColor(Color.GREEN)
                    artist_name.setTextColor(Color.GREEN)
                } else {
                    title_song.setTextColor(PrincipalColor)
                    artist_name.setTextColor(SecondaryColor)
                }

                if (adapterPosition in selectedSongs){
                    itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.selected_card_background))
                } else {
                    itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.deselected_card_background))
                }
        }
/*
        fun resetLastViewStyle(itemView: View){
            if (playingSong!=-1){
                title_song.setTextColor(PrincipalColor)
                artist_name.setTextColor(SecondaryColor)
            }
        }
*/
        override fun onClick(v: View?) {
            if (v != null) {
               // resetLastViewStyle()
                val lastPlayingSong: Int = playingSong
                onSongListener.onSongClick(adapterPosition, v)

                if (!editMode){
                    playingSong = adapterPosition
                    notifyItemChanged(lastPlayingSong)
                }else {
                    if (adapterPosition in selectedSongs) selectedSongs.remove(adapterPosition) else selectedSongs.add(adapterPosition)
                }

                updateViewStyle()
            }
        }



        override fun onLongClick(v: View?): Boolean {
            if (v != null) {
                if (editMode){
                    selectedSongs.removeAll{true}
                    onSongListener.onLongSongClick(adapterPosition, v)
                }else {
                    selectedSongs.add(adapterPosition)
                    onSongListener.onLongSongClick(adapterPosition, v)
                    updateViewStyle()
                }
                notifyDataSetChanged()
                return true
            }
            return false
        }
    }

    interface OnSongListener {
        fun onSongClick(position: Int, itemView: View)
        fun onLongSongClick(position: Int, itemView: View)
    }
}
