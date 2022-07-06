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
    //lateinit var toggle: ActionBarDrawerToggle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //val drawerlayout: DrawerLayout = binding!!.drawerLayout
        //val navi_view: NavigationView = binding!!.navView

        // Inflate the layout for this fragment
        binding =  FragmentHomeBinding.inflate(inflater, container, false)

        /*toggle = ActionBarDrawerToggle(Activity(), drawerlayout, R.string.open, R.string.close)
        drawerlayout.addDrawerListener(toggle)
        toggle.syncState()*/






        return binding!!.root
    }


}