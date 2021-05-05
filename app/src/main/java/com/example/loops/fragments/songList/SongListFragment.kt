package com.example.loops.fragments.songList

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loops.R
import com.example.loops.adapter.CustomAdapterSongList
import com.example.loops.model.Song


class SongListFragment : Fragment(), SearchView.OnQueryTextListener, View.OnClickListener, CustomAdapterSongList.OnSongListener {

    private lateinit var songsListViewModel: SongListViewModel
    private var song: MutableLiveData<Song> = MutableLiveData<Song>()

    private lateinit var fragmentView: View
    private var songView: CardView? = null

    private lateinit var adapter: CustomAdapterSongList

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(R.layout.fragment_songlist, container, false)
        (activity as AppCompatActivity?)!!.setSupportActionBar(fragmentView.findViewById(R.id.toolbar_songFragment))
        setHasOptionsMenu(true) //indicamos a la activity host (MainActivity) que el fragmento tiene items de menu que quiere añadir


        init()

        return fragmentView
    }

    private fun init() {
        setAdapter()

        songsListViewModel = ViewModelProvider(this).get(SongListViewModel::class.java)
        songsListViewModel.showAllDeviceSongs(adapter, fragmentView)

/*        //observador -> observa la variable MutableLiveData<List<Song>> de el viewModel, para que cuando cambie actualice la lista
        songsListViewModel.getSongsList().observe(viewLifecycleOwner, Observer { it ->
            it?.let {
                songsListViewModel.showAllDeviceSongs()
            }
        })*/
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_song_list, menu)
        val menuItem = menu.findItem(R.id.search)

        if (menuItem != null) {
            val searchView = menuItem.actionView as SearchView
            searchView.setQueryHint("Search...")
            searchView.setOnQueryTextListener(this)
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        val original = songsListViewModel.getListSongs().value
        if (original != null && query != null) {
            adapter.setSongs(original.filter { song -> song.title.toUpperCase().contains(query.toUpperCase()) })
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val original = songsListViewModel.getListSongs().value
        if (original != null && newText != null) {
            adapter.setSongs(original.filter { song -> song.title.toUpperCase().contains(newText.toUpperCase()) })
        }
        return false
    }


    private fun setAdapter() {
        adapter = activity?.let { CustomAdapterSongList(it.applicationContext, this, R.layout.custom_card_song, this) }!!
        fragmentView.findViewById<RecyclerView>(R.id.rv_deviceSongs).layoutManager = LinearLayoutManager(fragmentView.context)
        fragmentView.findViewById<RecyclerView>(R.id.rv_deviceSongs).adapter = adapter
    }


    override fun onClick(p0: View?) {
    }

    override fun onSongClick(position: Int, itemView: View) {
        songView?.findViewById<TextView>(R.id.title_song)?.setTextColor(ContextCompat.getColor(fragmentView.context, R.color.principal_text_color))
        songView?.findViewById<TextView>(R.id.artist_name)?.setTextColor(ContextCompat.getColor(fragmentView.context, R.color.principal_text_color))

        song.value = songsListViewModel.clickSong(position)

        songView = itemView as CardView
        itemView.findViewById<TextView>(R.id.title_song).setTextColor(Color.GREEN)
        itemView.findViewById<TextView>(R.id.artist_name).setTextColor(Color.GREEN)
    }

    fun getClickedSong(): LiveData<Song> {
        return song
    }




}