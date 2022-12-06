package com.example.hamarisawari.Fragments

import android.Manifest
import android.app.Activity.RESULT_OK
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
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.hamarisawari.Communicator
import com.example.hamarisawari.R
import com.example.hamarisawari.databinding.FragmentRentBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.ByteArrayOutputStream
import java.io.InputStream


class RentFragment : Fragment(R.layout.fragment_rent) {



    var encoded_image: ArrayList<String>? = ArrayList()
    private var binding : FragmentRentBinding?=null
    private lateinit var communicator: Communicator

    lateinit var pictures: ImageView
    lateinit var btnRentCar: Button
    lateinit var btnRentBike: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        binding = FragmentRentBinding.inflate(inflater, container, false)

        btnRentCar = binding!!.rentCarFragment
        btnRentBike = binding!!.rentBikeFragment
        pictures = binding!!.vhPicture

        shiftFragment(RentBikeFragment())

        btnRentCar.setOnClickListener {

            shiftFragment(RentCarFragment())
        }
        btnRentBike.setOnClickListener {

            shiftFragment(RentBikeFragment())
        }



        pictures.setOnClickListener{



            Dexter.withActivity(activity)
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
        return binding!!.root
    }
    private fun encodeBitmapImage(bitmap: Bitmap): String {

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val bytesofimage: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(bytesofimage, Base64.DEFAULT)
    }

    private fun shiftFragment(fragment: Fragment)
    {
        val frag = fragment
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.rentFragment, frag)
        transaction.commit()
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
                                activity?.contentResolver?.openInputStream(
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
                    Toast.makeText(context, "You can only select up to 5 images", Toast.LENGTH_LONG).show()

                }
            } else {

                val imageurl: Uri? = data.data
                try {
                    var inputStream: InputStream? = imageurl?.let {
                        activity?.contentResolver?.openInputStream(
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
            Toast.makeText(context, "Please select an image to upload", Toast.LENGTH_LONG).show()
        }




         communicator = activity as Communicator
         encoded_image?.let { communicator.passDataCom(it) }

         //Toast.makeText(context, encoded_image.toString(), Toast.LENGTH_LONG).show()
    }






}