package com.example.hamarisawari

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.ByteArrayOutputStream
import java.io.InputStream

class RentBike : AppCompatActivity() {

    var encoded_image: ArrayList<String>? = ArrayList()
    lateinit var pictures: ImageView

    lateinit var latitude: String
    lateinit var longitude: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rent_bike)


        var upload: Button= findViewById(R.id.uploadBike)
        pictures = findViewById(R.id.bikePicture)

        //fetching username
        var mySharedPref = getSharedPreferences("userInfo", MODE_PRIVATE)
        var username= mySharedPref.getString("username",null)

        //fetching user's current location to store it as Vehicle's location of rent.
        latitude = mySharedPref.getString("latitude",null).toString()
        longitude = mySharedPref.getString("longitude",null).toString()


        displayDropDown()   //calling function that displays the dropdown menu



        pictures.setOnClickListener{



            Dexter.withActivity(this@RentBike)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {


                        val intent = Intent()
                        // setting type to select to be image
                        intent.type = "image/*"
                        // allowing multiple image to be selected
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                        intent.action = Intent.ACTION_GET_CONTENT
                        startActivityForResult(
                            Intent.createChooser(intent, "Select Picture"),
                            1
                        )
                    }
                    override fun onPermissionDenied(response: PermissionDeniedResponse) {}
                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()




        }

        upload.setOnClickListener {

            if (username != null) {
                postAdd(username)
            }
        }
    }

    private fun encodeBitmapImage(bitmap: Bitmap): String {

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val bytesofimage: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(bytesofimage, Base64.DEFAULT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        // When an Image is picked
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            // Get the Image from data
            if (data.clipData != null) {
                val mClipData = data.clipData
                val count = data.clipData!!.itemCount

                if(count <= 5){

                    for (i in 0 until count) {
                        // adding imageuri in array

                        val imageurl: Uri = data.clipData!!.getItemAt(i).uri

                        try {
                            var inputStream: InputStream? = imageurl?.let {
                                this@RentBike?.contentResolver?.openInputStream(
                                    it
                                )
                            }
                            var bitmap = BitmapFactory.decodeStream(inputStream)
                            pictures.setImageBitmap(bitmap)

                            encoded_image?.add(encodeBitmapImage(bitmap))

                        } catch (ex: Exception) {
                        }

                    }


                }else {
                    Toast.makeText(this@RentBike, "You can only select up to 5 images", Toast.LENGTH_LONG).show()

                }
            } else {

                val imageurl: Uri? = data.data
                try {
                    var inputStream: InputStream? = imageurl?.let {
                        this@RentBike?.contentResolver?.openInputStream(
                            it
                        )
                    }
                    var bitmap = BitmapFactory.decodeStream(inputStream)
                    pictures.setImageBitmap(bitmap)

                    encoded_image?.add(encodeBitmapImage(bitmap).toString())

                } catch (ex: Exception) {
                }
            }
        } else {
            // show this if no image is selected
            Toast.makeText(this@RentBike, "Please select an image to upload", Toast.LENGTH_LONG).show()
        }



        //Toast.makeText(context, encoded_image.toString(), Toast.LENGTH_LONG).show()
    }

    private fun displayDropDown()
    {
        val manufacturer = resources.getStringArray(R.array.BikeManufacturer)
        val engine = resources.getStringArray(R.array.BikeEngine)
        val color = resources.getStringArray(R.array.BikeColor)
        val seatingCapacity = resources.getStringArray(R.array.seatingCapacityBike)
        val deliveryStatus = resources.getStringArray(R.array.deliveryStatus)



        val arrayAdapterManufacturer = ArrayAdapter(this@RentBike, R.layout.dropdown_menu, manufacturer)
        val arrayAdapterEngine = ArrayAdapter(this@RentBike, R.layout.dropdown_menu, engine)
        val arrayAdapterColor = ArrayAdapter(this@RentBike, R.layout.dropdown_menu, color)
        val arrayAdapterCondition = ArrayAdapter(this@RentBike, R.layout.dropdown_menu, seatingCapacity)
        val arrayAdapterDelivery = ArrayAdapter(this@RentBike, R.layout.dropdown_menu, deliveryStatus)


        findViewById<AutoCompleteTextView>(R.id.manufacturerBike).setAdapter(arrayAdapterManufacturer)
        findViewById<AutoCompleteTextView>(R.id.engineBike).setAdapter(arrayAdapterEngine)
        findViewById<AutoCompleteTextView>(R.id.colorBike).setAdapter(arrayAdapterColor)
        findViewById<AutoCompleteTextView>(R.id.seatingCapacityBike).setAdapter(arrayAdapterCondition)
        findViewById<AutoCompleteTextView>(R.id.deliveryBike).setAdapter(arrayAdapterDelivery)

    }

    private fun postAdd(username:String){

        var color = findViewById<AutoCompleteTextView>(R.id.colorBike).text.toString()
        var manufacturer = findViewById<AutoCompleteTextView>(R.id.manufacturerBike).text.toString()
        var seatingCapacity = findViewById<AutoCompleteTextView>(R.id.seatingCapacityBike).text.toString()
        var engineCapacity = findViewById<AutoCompleteTextView>(R.id.engineBike).text.toString()
        var deliveryStatus = findViewById<AutoCompleteTextView>(R.id.deliveryBike).text.toString()
        var mileage = findViewById<EditText>(R.id.mileageBike).text.toString()
        var bikeModel = findViewById<EditText>(R.id.modelBike).text.toString()
        var engineNumber = findViewById<EditText>(R.id.enginenumberBike).text.toString()
        var numberPlate = findViewById<EditText>(R.id.numberplateBike).text.toString()
        var description = findViewById<EditText>(R.id.BikeDis).text.toString()
        var price = findViewById<EditText>(R.id.rentingPriceBike).text.toString()


        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().rentBike_URL,
            Response.Listener { response ->

                Toast.makeText(this@RentBike, response.toString(), Toast.LENGTH_LONG).show()

                print("${response.toString()}")
            },
            Response.ErrorListener { error ->

                Toast.makeText(this@RentBike, error.toString(), Toast.LENGTH_LONG).show()

                print("${error.toString()}")
            }){

            override fun getParams(): Map<String, String> {


                val map : MutableMap<String,String> = HashMap()

                map["username"] = username
                map["rentingPrice"] = price
                map["color"] = color
                map["manufacturer"] = manufacturer
                map["seatingCapacity"] = seatingCapacity
                map["engineCapacity"] = engineCapacity
                map["mileage"] = mileage
                map["bikeModel"] = bikeModel
                map["engineNumber"] = engineNumber
                map["numberPlate"] = numberPlate
                map["description"] = description
                map["delivery"] = deliveryStatus
                map["latitude"] = latitude
                map["longitude"] = longitude

                for(i in 0 until encoded_image?.size!!){

                    map["image" + i] = encoded_image?.get(i)!!

                }
                return map
            }
        }

        val queue = Volley.newRequestQueue(this@RentBike)
        queue.add(request)
    }

}