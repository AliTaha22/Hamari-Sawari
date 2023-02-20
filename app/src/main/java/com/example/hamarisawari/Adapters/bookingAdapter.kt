package com.example.hamarisawari.com.example.hamarisawari.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hamarisawari.*
import com.example.hamarisawari.Fragments.MoreDetailsFragment
import com.example.hamarisawari.com.example.hamarisawari.bookingDataClass

class bookingAdapter(_ctx: Context, _data:ArrayList<bookingDataClass>, _myUsername: String): RecyclerView.Adapter<bookingAdapter.homeViewHolder>() {

    var ctx=_ctx
    var data=_data
    var myUsername = _myUsername



    class homeViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        var bookingidview: TextView =itemView.findViewById(R.id.bookingID)
        var renterview: TextView =itemView.findViewById(R.id.renterID)
        var renteeview: TextView =itemView.findViewById(R.id.renteeID)
        var typeview: TextView =itemView.findViewById(R.id.vehicleTypeID)
        var statusview: TextView = itemView.findViewById(R.id.statusID)
        var btn: Button = itemView.findViewById(R.id.viewBookingButton)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): homeViewHolder {

        var itemView= LayoutInflater.from(ctx).inflate(R.layout.bookingadapter,parent,false)


        return homeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: homeViewHolder, @SuppressLint("RecyclerView") position: Int) {

        holder.bookingidview.text = data[position].id
        holder.renterview.text= data[position].renter
        holder.renteeview.text = data[position].rentee
        holder.typeview.text = data[position].type
        holder.statusview.text = data[position].status


        holder.btn.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val activity = v!!.context as AppCompatActivity

                if(data[position].status == "Initialized"){

                    if(myUsername == data[position].rentee){

                        val i = Intent(activity, ContactAndCommunications::class.java)

                        val bundle = Bundle()
                        bundle.putString("latitude", data[position].renterlat)
                        bundle.putString("longitude", data[position].renterlong)
                        bundle.putString("username", data[position].renter)
                        bundle.putString("vhtype", data[position].type)
                        bundle.putString("vhnumberplate", data[position].numberplate)
                        bundle.putString("vhprice", data[position].vhPrice)

                        i.putExtras(bundle)
                        activity.startActivity(i)
                    }
                    else{
                        val i = Intent(activity, ContactAndCommunicationsRenter::class.java)

                        val bundle = Bundle()
                        bundle.putString("rentee", data[position].rentee)
                        bundle.putString("renter", data[position].renter)
                        bundle.putString("VehicleType", data[position].type)
                        bundle.putString("Numberplate", data[position].numberplate)
                        bundle.putString("renteeLatitude", data[position].renteelat)
                        bundle.putString("renteeLongitude", data[position].renteelong)


                        i.putExtras(bundle)
                        activity.startActivity(i)
                    }
                }
                else {

                    val i = Intent(activity, CurrentlyActiveBooking::class.java)

                    val bundle = Bundle()
                    bundle.putString("renter", data[position].renter)
                    bundle.putString("rentee", data[position].rentee)
                    bundle.putString("numberplate", data[position].numberplate)
                    bundle.putString("price", data[position].price)
                    bundle.putString("id", data[position].id)


                    i.putExtras(bundle)
                    activity.startActivity(i)
                }


            }
        })
    }

    override fun getItemCount(): Int {
        return data.size

    }

}