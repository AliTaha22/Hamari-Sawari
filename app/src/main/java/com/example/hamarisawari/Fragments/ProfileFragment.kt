package com.example.hamarisawari.Fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
import java.io.InputStream


class ProfileFragment : Fragment(R.layout.fragment_profile) {




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
            Dexter.withActivity(activity)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.type = "image/*"
                        startActivityForResult(Intent.createChooser(intent, "Browse Image"), 1)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            val filepath: Uri? = data?.data
            try {
                var inputStream: InputStream? = filepath?.let {
                    activity?.contentResolver?.openInputStream(
                        it
                    )
                }
                bitmap = BitmapFactory.decodeStream(inputStream)
                profilepic.setImageBitmap(bitmap)
                encodeBitmapImage(bitmap)
            } catch (ex: Exception) {
            }
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