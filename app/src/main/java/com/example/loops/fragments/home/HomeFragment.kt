package com.example.loops.fragments.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.loops.R
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var fragmentView: View

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        fragmentView = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = fragmentView.findViewById(R.id.text_home_welcome)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        init()
        return fragmentView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun init(){
        if (FirebaseAuth.getInstance().currentUser!=null){
            fragmentView.findViewById<TextView>(R.id.text_home_welcome).text = "Buenos dias " + FirebaseAuth.getInstance().currentUser.displayName + "..."
        } else {
            fragmentView.findViewById<TextView>(R.id.text_home_welcome).text = "Buenos dias..."
        }
    }



}