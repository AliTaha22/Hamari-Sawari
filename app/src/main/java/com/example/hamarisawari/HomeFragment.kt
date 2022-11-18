package com.example.hamarisawari

import android.app.Activity
import android.app.Notification
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.hamarisawari.databinding.FragmentCarSearchBinding
import com.example.hamarisawari.databinding.FragmentHomeBinding
import com.google.android.material.navigation.NavigationView


class HomeFragment : Fragment(R.layout.fragment_home ) {

    lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        binding =  FragmentHomeBinding.inflate(inflater, container, false)







        return binding!!.root
    }


}