package com.example.hamarisawari

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction


class SearchFragment : Fragment(R.layout.fragment_search) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         val view:View = inflater.inflate(R.layout.fragment_search, container, false)

        var btnSearchCar:Button = view.findViewById(R.id.searchCarFragment)
        var btnSearchBike:Button = view.findViewById(R.id.searchBikeFragment)


        shiftFragment(SearchCarFragment())

        btnSearchCar.setOnClickListener {

            shiftFragment(SearchCarFragment())
        }
        btnSearchBike.setOnClickListener {

            shiftFragment(SearchBikeFragment())
        }

        return view
    }


    private fun shiftFragment(fragment: Fragment)
    {
        val frag = fragment
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.searchFragment, frag)
        transaction.commit()
    }

}