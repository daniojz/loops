package com.example.loops.fragments.songList

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loops.R
import com.example.loops.adapter.CustomAdapterSongList
import com.example.loops.model.Song
import com.google.firebase.auth.FirebaseAuth


class SongListFragment : Fragment(), SearchView.OnQueryTextListener, View.OnClickListener, CustomAdapterSongList.OnSongListener {

    private lateinit var songsListViewModel: SongListViewModel

    private lateinit var fragmentView: View
    private lateinit var toolbarMenu: Menu
    private lateinit var recyclerView: RecyclerView

    private var song: MutableLiveData<Song> = MutableLiveData<Song>()
    private var selectedSongsList: ArrayList<Song> = ArrayList<Song>()

    private lateinit var adapter: CustomAdapterSongList

    private var editMode = false

    private lateinit var tracker: SelectionTracker<Long>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(R.layout.fragment_songlist, container, false)
        (activity as AppCompatActivity?)!!.setSupportActionBar(fragmentView.findViewById(R.id.toolbar_songFragment))
        setHasOptionsMenu(true) //indicamos a la activity host (MainActivity) que el fragmento tiene items de menu que quiere añadir

        toolbarMenu = fragmentView.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_songFragment).menu
        recyclerView = fragmentView.findViewById(R.id.rv_deviceSongs)

        init()

        return fragmentView
    }

    private fun init() {
        setAdapter()

        songsListViewModel = ViewModelProvider(this).get(SongListViewModel::class.java)
        songsListViewModel.showAllDeviceSongs(adapter, fragmentView)

        trackSelectedItems()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_song_list, menu)
        val menuItem = menu.findItem(R.id.search_song)

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

    override fun onSongClick(position: Int, itemView: View) {
        if (!editMode) {
            song.value = songsListViewModel.clickSong(position)
        } else {
            if (itemView.isSelected) {
                deselectCard(itemView)
            } else {
                selectCard(itemView)
            }

        }

    }

    override fun onLongSongClick(position: Int, itemView: View) = if (!editMode) {
        editMode = true
        adapter.editMode = true

        menuEdit(true)
        selectCard(itemView)
    } else {
        editMode = false
        adapter.editMode = false

        menuEdit(false)
    }


    override fun onClick(p0: View?) {
    }


    private fun setAdapter() {
        adapter = activity?.let { CustomAdapterSongList(it.applicationContext, this, R.layout.custom_card_song, this) }!!
        recyclerView.layoutManager = LinearLayoutManager(fragmentView.context)
        recyclerView.adapter = adapter
    }

    fun getClickedSong(): LiveData<Song> {
        return song
    }

    private fun selectCard(itemView: View) {
        selectedSongsList.add(itemView.tag as Song)
    }

    private fun deselectCard(itemView: View) {
        selectedSongsList.remove(itemView.tag as Song)
    }

    private fun trackSelectedItems(){
        tracker = SelectionTracker.Builder<Long>(
            "mySelection",
            recyclerView,
            ItemIdKeyProvider(recyclerView),
            ItemLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        adapter.trackerSelection = tracker

        tracker?.addObserver(object: SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionChanged() {

            }
        })
    }

    private fun menuEdit(menuEdit: Boolean){
        if (menuEdit) {
            toolbarMenu.findItem(R.id.delete_song).isVisible = true
            toolbarMenu.findItem(R.id.upload_song).isVisible = true
        } else{
            toolbarMenu.findItem(R.id.delete_song).isVisible = false
            toolbarMenu.findItem(R.id.upload_song).isVisible = false
        }
    }

    fun uploadSongs(){
        val emailAuth = FirebaseAuth.getInstance().currentUser.email
    }




    inner class ItemIdKeyProvider(private val recyclerView: RecyclerView)
        : ItemKeyProvider<Long>(SCOPE_MAPPED) {

        override fun getKey(position: Int): Long? {
            return recyclerView.adapter?.getItemId(position)
                ?: throw IllegalStateException("RecyclerView adapter is not set!")
        }

        override fun getPosition(key: Long): Int {
            val viewHolder = recyclerView.findViewHolderForItemId(key)
            return viewHolder?.layoutPosition ?: RecyclerView.NO_POSITION
        }
    }

    inner class ItemLookup(private val rv: RecyclerView)
        : ItemDetailsLookup<Long>() {
        override fun getItemDetails(event: MotionEvent)
                : ItemDetails<Long>? {

            val view = rv.findChildViewUnder(event.x, event.y)
            if(view != null) {
                return (rv.getChildViewHolder(view) as CustomAdapterSongList.ViewHolder)
                    .getItemDetails()
            }
            return null
        }
    }

}
