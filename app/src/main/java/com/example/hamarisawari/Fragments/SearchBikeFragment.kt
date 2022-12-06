package com.example.hamarisawari.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.example.hamarisawari.R
import com.example.hamarisawari.databinding.FragmentBikeSearchBinding


class SearchBikeFragment : Fragment(R.layout.fragment_bike_search) {



    private var binding : FragmentBikeSearchBinding? = null


    override fun onResume() {
        super.onResume()

        displayDropDown()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBikeSearchBinding.inflate(inflater, container, false)
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
        val manufacturer = resources.getStringArray(R.array.BikeManufacturer)
        val engine = resources.getStringArray(R.array.BikeEngine)
        val color = resources.getStringArray(R.array.BikeColor)


        val arrayAdapterManufacturer = ArrayAdapter(requireContext(),
            R.layout.dropdown_menu, manufacturer)
        val arrayAdapterEngine = ArrayAdapter(requireContext(), R.layout.dropdown_menu, engine)
        val arrayAdapterColor = ArrayAdapter(requireContext(), R.layout.dropdown_menu, color)



        binding!!.manufacturer.setAdapter(arrayAdapterManufacturer)
        binding!!.engine.setAdapter(arrayAdapterEngine)
        binding!!.color.setAdapter(arrayAdapterColor)
    }
}