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
        val text_home_welcome: TextView = fragmentView.findViewById(R.id.text_home_welcome)


        if (FirebaseAuth.getInstance().currentUser!=null){
            text_home_welcome.text = "Buenos dias " + FirebaseAuth.getInstance().currentUser.displayName + "..."
        } else {
            text_home_welcome.text = "Buenos dias..."
        }

        return fragmentView
    }




}