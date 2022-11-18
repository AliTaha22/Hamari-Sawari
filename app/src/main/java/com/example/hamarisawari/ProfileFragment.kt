package com.example.hamarisawari

import android.Manifest
import android.R.attr
import android.R.attr.bitmap
import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hamarisawari.databinding.FragmentProfileBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.ByteArrayOutputStream
import java.io.InputStream


class ProfileFragment : Fragment(R.layout.fragment_profile) {




    private var binding : FragmentProfileBinding?=null

    private var url = "http://192.168.178.1/hamarisawari/fileupload.php"
    lateinit var profilepic:ImageView
    lateinit var uploadpic:Button
    lateinit var encoded_image: String
    lateinit var bitmap: Bitmap
    lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)


        var mySharedPref = context?.getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE)
        username = mySharedPref!!.getString("username", null).toString()


        profilepic = binding!!.profileImg
        uploadpic = binding!!.uploadPic

        var name = binding!!.Name
        var age = binding!!.Age
        var contact = binding!!.Contact
        var email = binding!!.Email

        profilepic.setOnClickListener(View.OnClickListener {
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
        })

        uploadpic.setOnClickListener(View.OnClickListener { uploadDataToDB() })


        return binding!!.root
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
        super.onActivityResult(requestCode, resultCode, data)
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
            Request.Method.POST, url,
            Response.Listener { response ->

                profilepic.setImageResource(R.drawable.ic_profile)
                Toast.makeText(
                    context,
                    response, Toast.LENGTH_LONG
                ).show()
            },
            Response.ErrorListener { error ->
                Toast.makeText(
                    context,
                    error.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }) {
            override fun getParams(): Map<String, String> {
                val map: MutableMap<String, String> = HashMap()
                map["upload"] = encoded_image
                map["username"] = username.toString()
                return map
            }
        }


        val queue = Volley.newRequestQueue(requireActivity().applicationContext)
        queue.add(request)

    }

    private fun fetchUserInfo()
    {
        val url = "http://192.168.178.1/hamarisawari/profileInfo.php"
        val request: StringRequest = object : StringRequest(Request.Method.POST, url,

            Response.Listener { response -> },
            Response.ErrorListener { error -> }){

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