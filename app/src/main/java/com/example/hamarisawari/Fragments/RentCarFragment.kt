package com.example.hamarisawari.Fragments

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hamarisawari.R
import com.example.hamarisawari.URLs
import com.example.hamarisawari.databinding.FragmentRentCarBinding


class RentCarFragment : Fragment(R.layout.fragment_rent_car) {
    private var binding : FragmentRentCarBinding? = null
    var encoded_image: ArrayList<String>? = ArrayList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentRentCarBinding.inflate(inflater, container, false)
        var upload=binding!!.btnUpload


        displayDropDown()   //calling function that displays the dropdown menu



        var mySharedPref = context?.getSharedPreferences("userInfo", MODE_PRIVATE)
        var username= mySharedPref?.getString("username",null)





        val bundle = this.arguments
        if (bundle != null) {
            encoded_image = bundle.getStringArrayList("images") as ArrayList<String>
        }


        //seekbars


        upload.setOnClickListener {

            //var m=color.toString()
            //Toast.makeText(context, "$m", Toast.LENGTH_SHORT).show()
            if (username != null) {
                postAdd(username)
            }
        }

        return binding!!.root
    }



    override fun onResume() {

        displayDropDown()
        super.onResume()

    }



    private fun displayDropDown()
    {
        val manufacturer = resources.getStringArray(R.array.Manufacturer)
        val transmission = resources.getStringArray(R.array.Transmission)
        val type = resources.getStringArray(R.array.Type)
        val condition = resources.getStringArray(R.array.Condition)
        val color = resources.getStringArray(R.array.CarColor)
        val engine = resources.getStringArray(R.array.CarEngine)


        val arrayAdapterManufacturer = ArrayAdapter(requireContext(),
            R.layout.dropdown_menu, manufacturer)
        val arrayAdapterTransmission = ArrayAdapter(requireContext(),
            R.layout.dropdown_menu, transmission)
        val arrayAdapterType = ArrayAdapter(requireContext(), R.layout.dropdown_menu, type)
        val arrayAdapterCondition = ArrayAdapter(requireContext(), R.layout.dropdown_menu, condition)
        val arrayAdapterColor = ArrayAdapter(requireContext(), R.layout.dropdown_menu, color)
        val arrayAdapterEngine = ArrayAdapter(requireContext(), R.layout.dropdown_menu, engine)



        binding!!.manufacturer.setAdapter(arrayAdapterManufacturer)
        binding!!.transmission.setAdapter(arrayAdapterTransmission)
        binding!!.type.setAdapter(arrayAdapterType)
        binding!!.condition.setAdapter(arrayAdapterCondition)
        binding!!.color.setAdapter(arrayAdapterColor)
        binding!!.engine.setAdapter(arrayAdapterEngine)
    }

    private fun postAdd(username: String){


        //All the data from rent car Fragment.
        var color=binding!!.color.text.toString()
        var manufacturer=binding!!.manufacturer.text.toString()
        var condition=binding!!.condition.text.toString()
        var transmission=binding!!.transmission.text.toString()
        var type=binding!!.type.text.toString()
        var engineCapacity=binding!!.engine.text.toString()
        var mileage=binding!!.CarMilage.text.toString()
        var carModel=binding!!.carModel.text.toString()
        var engineNumber=binding!!.EngineNumber.text.toString()
        var numberPlate=binding!!.NumberPlate.text.toString()
        var description=binding!!.CarDis.text.toString()
        var price = binding!!.rentingPriceCar.text.toString()


        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().rentCar_URL,
            Response.Listener { response ->

                Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show()

                Log.d("My Error:", response.toString() )
            },
            Response.ErrorListener { error ->

                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()

                Log.d("My Error:", error.toString() )
            }){

            override fun getParams(): Map<String, String> {


                val map : MutableMap<String,String> = HashMap()


                map["username"] = username
                map["rentingPrice"] = price
                map["color"] = color
                map["manufacturer"] = manufacturer
                map["condition"] = condition
                map["transmission"] = transmission
                map["type"] = type
                map["engineCapacity"] = engineCapacity
                map["mileage"] = mileage
                map["carModel"] = carModel
                map["engineNumber"] = engineNumber
                map["numberPlate"] = numberPlate
                map["description"] = description


                //Log.d("IMG_SIZE: " , encoded_image?.size!!.toString())

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
        binding = null

        super.onDestroy()

    }

}
