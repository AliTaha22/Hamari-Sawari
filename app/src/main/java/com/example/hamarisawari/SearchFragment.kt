package com.example.hamarisawari

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction


class SearchFragment : Fragment(R.layout.fragment_search) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         val view:View = inflater.inflate(R.layout.fragment_search, container, false)

        var btn1:Button = view.findViewById(R.id.carFragment)
        var btn2:Button = view.findViewById(R.id.bikeFragment)
        var btn3:Button = view.findViewById(R.id.truckFragment)

        btn1.setOnClickListener {

            val carFG = CarFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragmentContainer3, carFG)
            transaction.commit()
        }
        btn2.setOnClickListener {
            val bikeFG = BikeFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragmentContainer3, bikeFG)
            transaction.commit()
        }
        btn3.setOnClickListener {
            val truckFG = TruckFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragmentContainer3, truckFG)
            transaction.commit()
        }

        return view
    }

}