package com.example.hamarisawari.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SeekBar
import com.example.hamarisawari.R
import com.example.hamarisawari.databinding.FragmentCarSearchBinding


class SearchCarFragment : Fragment(R.layout.fragment_car_search) {

    private var binding: FragmentCarSearchBinding? = null



    override fun onResume() {
        super.onResume()

        displayDropDown()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentCarSearchBinding.inflate(inflater, container, false)

        var priceSB: SeekBar = binding!!.priceSeekBar
        var locationSB: SeekBar = binding!!.locationSeekBar
        var priceBar = binding!!.priceBar
        var locationBar = binding!!. locationBar


        displayDropDown()   //calling function that displays the dropdown menu

        //seekbars
        priceSB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                priceBar.text = progress.toString()

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        locationSB.setOnSeekBarChangeListener(object :  SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                locationBar.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })


        return binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }


    private fun displayDropDown()
    {
        val manufacturer = resources.getStringArray(R.array.Manufacturer)
        val transmission = resources.getStringArray(R.array.Transmission)
        val type = resources.getStringArray(R.array.Type)


        val arrayAdapterManufacturer = ArrayAdapter(requireContext(),
            R.layout.dropdown_menu, manufacturer)
        val arrayAdapterTransmission = ArrayAdapter(requireContext(),
            R.layout.dropdown_menu, transmission)
        val arrayAdapterType = ArrayAdapter(requireContext(), R.layout.dropdown_menu, type)



        binding!!.manufacturer.setAdapter(arrayAdapterManufacturer)
        binding!!.transmission.setAdapter(arrayAdapterTransmission)
        binding!!.type.setAdapter(arrayAdapterType)
    }

}