package com.example.hamarisawari.com.example.hamarisawari.Adapters

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
import com.example.hamarisawari.CurrentlyActiveBooking
import com.example.hamarisawari.Fragments.MoreDetailsFragment
import com.example.hamarisawari.R
import com.example.hamarisawari.URLs
import com.example.hamarisawari.com.example.hamarisawari.bookingDataClass
import com.example.hamarisawari.vehicles

class bookingAdapter(_ctx: Context, _data:ArrayList<bookingDataClass>): RecyclerView.Adapter<bookingAdapter.homeViewHolder>() {

    var ctx=_ctx
    var data=_data



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

    override fun onBindViewHolder(holder: homeViewHolder, position: Int) {

        holder.bookingidview.text = data[position].id
        holder.renterview.text= data[position].renter
        holder.renteeview.text = data[position].rentee
        holder.typeview.text = data[position].type
        holder.statusview.text = data[position].status


        holder.btn.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val activity = v!!.context as AppCompatActivity

                activity.startActivity(Intent(activity, CurrentlyActiveBooking::class.java))

            }
        })
    }

    override fun getItemCount(): Int {
        return data.size

    }

}