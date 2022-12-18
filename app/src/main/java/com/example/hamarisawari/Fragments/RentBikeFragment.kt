package com.example.hamarisawari.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hamarisawari.R
import com.example.hamarisawari.URLs
import com.example.hamarisawari.databinding.FragmentRentBikeBinding


class RentBikeFragment : Fragment(R.layout.fragment_rent_bike) {

    private var binding : FragmentRentBikeBinding?=null
    var encoded_image: ArrayList<String>? = ArrayList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentRentBikeBinding.inflate(inflater, container, false)
        var upload=binding!!.btnUpload


        var mySharedPref = context?.getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE)
        var username= mySharedPref?.getString("username",null)

        val bundle = this.arguments
        if (bundle != null) {
            encoded_image = bundle.getStringArrayList("images") as ArrayList<String>
        }

        displayDropDown()   //calling function that displays the dropdown menu



        upload.setOnClickListener {

            if (username != null) {
                postAdd(username)
            }
        }


        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
        displayDropDown()

    }

    private fun displayDropDown()
    {
        val manufacturer = resources.getStringArray(R.array.BikeManufacturer)
        val engine = resources.getStringArray(R.array.BikeEngine)
        val color = resources.getStringArray(R.array.BikeColor)
        val condition = resources.getStringArray(R.array.Condition)


        val arrayAdapterManufacturer = ArrayAdapter(requireContext(),
            R.layout.dropdown_menu, manufacturer)
        val arrayAdapterEngine = ArrayAdapter(requireContext(), R.layout.dropdown_menu, engine)
        val arrayAdapterColor = ArrayAdapter(requireContext(), R.layout.dropdown_menu, color)
        val arrayAdapterCondition = ArrayAdapter(requireContext(), R.layout.dropdown_menu, condition)



        binding!!.manufacturer.setAdapter(arrayAdapterManufacturer)
        binding!!.engine.setAdapter(arrayAdapterEngine)
        binding!!.color.setAdapter(arrayAdapterColor)
        binding!!.condition.setAdapter(arrayAdapterCondition)
    }

    private fun postAdd(username:String){


        var color=binding!!.color.text.toString()
        var manufacturer=binding!!.manufacturer.text.toString()
        var condition=binding!!.condition.text.toString()
        var engineCapacity=binding!!.engine.text.toString()
        var mileage=binding!!.BikeMilage.text.toString()
        var bikeModel=binding!!.BikeModel.text.toString()
        var engineNumber=binding!!.EngineNumber.text.toString()
        var numberPlate=binding!!.NumberPlate.text.toString()
        var description=binding!!.BikeDis.text.toString()
        var price=binding!!.rentingPriceBike.text.toString()


        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().rentBike_URL,
            Response.Listener { response ->

                Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show()

                print("${response.toString()}")
            },
            Response.ErrorListener { error ->

                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()

                print("${error.toString()}")
            }){

            override fun getParams(): Map<String, String> {


                val map : MutableMap<String,String> = HashMap()

                map["username"] = username
                map["rentingPrice"] = price
                map["color"] = color
                map["manufacturer"] = manufacturer
                map["condition"] = condition
                map["engineCapacity"] = engineCapacity
                map["mileage"] = mileage
                map["bikeModel"] = bikeModel
                map["engineNumber"] = engineNumber
                map["numberPlate"] = numberPlate
                map["description"] = description

                for(i in 0 until encoded_image?.size!!){

                    map["image" + i] = encoded_image?.get(i)!!

                }
                return map
            }
        }

        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null

    }

}