package com.example.hamarisawari

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction

class RentFragment : Fragment(R.layout.fragment_rent) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_rent, container, false)
        var btnRentCar: Button = view.findViewById(R.id.rentCarFragment)
        var btnRentBike: Button = view.findViewById(R.id.rentBikeFragment)
        shiftFragment(RentCarFragment())

        btnRentCar.setOnClickListener {

            shiftFragment(RentCarFragment())
        }
        btnRentBike.setOnClickListener {

            shiftFragment(RentBikeFragment())
        }






        return view
    }
    private fun shiftFragment(fragment: Fragment)
    {
        val frag = fragment
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.rentFragment, frag)
        transaction.commit()
    }

}