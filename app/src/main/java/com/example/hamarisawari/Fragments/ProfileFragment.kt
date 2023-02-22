package com.example.hamarisawari.Fragments

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.hamarisawari.R
import com.example.hamarisawari.URLs
import com.example.hamarisawari.databinding.FragmentProfileBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val IMAGE_PICK_CODE = 1000
    private val PERMISSION_CODE = 1001


    private var binding : FragmentProfileBinding?=null

    lateinit var profilepic:ImageView
    lateinit var uploadpic:Button
    lateinit var encoded_image: String
    lateinit var bitmap: Bitmap
    lateinit var username: String

    lateinit var usernameTV: TextView
    lateinit var name: TextView
    lateinit var contact: TextView
    lateinit var cnic: TextView
    lateinit var verifyCnicImg:ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        verifyCnicImg = binding!!.verified
        verifyCnicImg.visibility = View.GONE


        var mySharedPref = context?.getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE)
        username = mySharedPref!!.getString("username", null).toString()


        profilepic = binding!!.profileImg
        uploadpic = binding!!.uploadPic


        name = binding!!.Name
        contact = binding!!.Contact
        cnic = binding!!.CNIC
        usernameTV = binding!!.username
        usernameTV.text = username


        fetchUserInfo()

        profilepic.setOnClickListener {
            // Check for permission to access gallery
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context?.let { it1 -> ContextCompat.checkSelfPermission(it1, Manifest.permission.READ_EXTERNAL_STORAGE) } == PackageManager.PERMISSION_DENIED) {
                    // Permission not granted, request it
                    ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_CODE)
                } else {
                    // Permission already granted, open gallery
                    openGallery()
                }
            } else {
                // System is lower than Marshmallow, no need to check for permission, open gallery
                openGallery()
            }
        }

        uploadpic.setOnClickListener { uploadDataToDB() }

        val verifyCnicButton = binding!!.verifyCnicButton

        verifyCnicButton.setOnClickListener {
            showVerifyCnicDialog()
        }


        return binding!!.root
    }


    private fun showVerifyCnicDialog() {
        var builder = context?.let { AlertDialog.Builder(it) }
        if (builder != null) {
            builder.setTitle("Verify CNIC")
        }

        // Set up the layout of the dialog
        val layout = LinearLayout(requireActivity().applicationContext)
        layout.orientation = LinearLayout.VERTICAL

        // Add an EditText for the user's CNIC
        val cnicEditText = EditText(requireActivity().applicationContext)
        cnicEditText.hint = "Enter your CNIC"
        layout.addView(cnicEditText)

        if (builder != null) {
            builder.setView(layout)
        }

        // Set up the buttons
        if (builder != null) {
            builder.setPositiveButton("Submit") { _, _ ->
                // When the user clicks "Submit", check the user's CNIC against the database
                val _cnic = cnicEditText.text.toString()
                verifyCnic(_cnic) // Replace with your own function to verify the CNIC

            }
        }
        if (builder != null) {
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
        }

        if (builder != null) {
            builder.show()
        }
    }

    private fun verifyCnic(_cnic: String){

        Log.d("CNIC:", _cnic.toString() )
        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().verifyCNIC_URL,
            Response.Listener { response ->


                Log.d("My Response:", response.toString() )
                if(response.contains("Success")){

                        // If the CNIC is verified, show a green tick or "Verified" text
                        Toast.makeText(requireActivity().applicationContext, "Verified", Toast.LENGTH_SHORT).show()
                        verifyCnicImg.visibility = View.VISIBLE
                        cnic.text = _cnic
                } else {
                    // If the CNIC is not verified, show an error message
                    Toast.makeText(requireActivity().applicationContext, "CNIC not found", Toast.LENGTH_SHORT).show()
                }


            },
            Response.ErrorListener { error ->
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): Map<String, String> {
                val map: MutableMap<String, String> = HashMap()
                map["cnic"] = _cnic
                map["username"] = username
                return map
            }
        }

        val queue = Volley.newRequestQueue(requireActivity().applicationContext)
        queue.add(request)


    }

    // Function to open gallery
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    // Function to handle permission request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, open gallery
                    openGallery()
                } else {
                    // Permission denied, show error message or do something else
                }
            }
        }
    }

    // Function to handle gallery selection result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            // Get selected image URI
            val imageUri = data?.data
            // Do something with the selected image URI, such as display it in an ImageView
        }
    }

    private fun encodeBitmapImage(bitmap: Bitmap) {

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val bytesofimage: ByteArray = byteArrayOutputStream.toByteArray()
        encoded_image = Base64.encodeToString(bytesofimage, Base64.DEFAULT)

    }

    private fun uploadDataToDB()
    {
        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().fileUpload_URL,
            Response.Listener { response ->

                Toast.makeText(context, response, Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener { error ->
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()

            }) {
            override fun getParams(): Map<String, String> {
                val map: MutableMap<String, String> = HashMap()
                map["upload"] = encoded_image
                map["username"] = username
                return map
            }
        }
        val queue = Volley.newRequestQueue(requireActivity().applicationContext)
        queue.add(request)

    }

    private fun fetchUserInfo()
    {


        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().profileInfo_URL,
            Response.Listener { response ->

                val jsonObject = JSONObject(response)

                name.text = jsonObject.getString("name")
                contact.text = jsonObject.getString("contact")
                cnic.text = jsonObject.getString("cnic")
                if(jsonObject.getString("verified") == "1"){
                    verifyCnicImg.visibility = View.VISIBLE
                }


                val dest =  URLs().images_URL + jsonObject.getString("picture")
                context?.let { Glide.with(it).load(dest).into(profilepic) }
            },
            Response.ErrorListener { error ->
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): Map<String, String> {
                val map: MutableMap<String, String> = HashMap()
                map["username"] = username
                return map
            }
        }

        val queue = Volley.newRequestQueue(requireActivity().applicationContext)
        queue.add(request)
    }

}