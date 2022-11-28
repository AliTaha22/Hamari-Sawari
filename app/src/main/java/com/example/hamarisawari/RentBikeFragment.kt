package com.example.hamarisawari

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hamarisawari.databinding.FragmentRentBikeBinding
import com.example.hamarisawari.databinding.FragmentRentCarBinding


class RentBikeFragment : Fragment(R.layout.fragment_rent_bike) {

    private var binding : FragmentRentBikeBinding?=null
    private val url = "http://192.168.100.157/hamarisawari/postbike.php"
    override fun onResume() {
        super.onResume()

        displayDropDown()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentRentBikeBinding.inflate(inflater, container, false)

        var priceSB: SeekBar = binding!!.priceSeekBar
        var priceBar = binding!!.priceBar
        displayDropDown()   //calling function that displays the dropdown menu
        var color=binding!!.color
        var upload=binding!!.btnUpload
        var manufacture=binding!!.manufacturer
        var condition=binding!!.condition
        var engineCapacity=binding!!.engine
        var milage=binding!!.BikeMilage
        var bikeModel=binding!!.BikeModel
        var engineNumber=binding!!.EngineNumber
        var numberPlate=binding!!.NumberPlate
        var description=binding!!.BikeDis
        var mySharedPref = this.getActivity()?.getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE)
        var userName= mySharedPref?.getString("username","unknown")

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

        upload.setOnClickListener {

            //var m=color.toString()
            //Toast.makeText(context, "$m", Toast.LENGTH_SHORT).show()
            if (userName != null) {
                postAdd(userName,priceBar.text.toString(),color.text.toString(),manufacture.text.toString(),condition.text.toString(),engineCapacity.text.toString(),milage.text.toString(),bikeModel.text.toString(),engineNumber.text.toString(),numberPlate.text.toString(),description.text.toString())
            }
        }


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
        val condition = resources.getStringArray(R.array.Condition)


        val arrayAdapterManufacturer = ArrayAdapter(requireContext(), R.layout.dropdown_menu, manufacturer)
        val arrayAdapterEngine = ArrayAdapter(requireContext(), R.layout.dropdown_menu, engine)
        val arrayAdapterColor = ArrayAdapter(requireContext(), R.layout.dropdown_menu, color)
        val arrayAdapterCondition = ArrayAdapter(requireContext(), R.layout.dropdown_menu, condition)



        binding!!.manufacturer.setAdapter(arrayAdapterManufacturer)
        binding!!.engine.setAdapter(arrayAdapterEngine)
        binding!!.color.setAdapter(arrayAdapterColor)
        binding!!.condition.setAdapter(arrayAdapterCondition)
    }

    private fun postAdd(userName:String,priceBar:String,color:String,manufacture:String,condition:String,engineCapacity:String,milage:String,bikeModel:String,engineNumber:String,numberPlate:String,description:String){

        val request: StringRequest = object : StringRequest(
            Request.Method.POST, url,
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

                map["userName"] = userName
                map["priceBar"] = priceBar
                map["color"] = color
                map["manufacture"] = manufacture
                map["condition"] = condition
                map["engineCapacity"] = engineCapacity
                map["milage"] = milage
                map["bikeModel"] = bikeModel
                map["engineNumber"] = engineNumber
                map["numberPlate"] = numberPlate
                map["description"] = description
                return map
            }
        }

        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }

}