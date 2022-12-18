package com.example.hamarisawari

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class homeAddAdapter(_ctx:Context,_data:ArrayList<vehicles>): RecyclerView.Adapter<homeAddAdapter.homeViewHolder>() {

    var ctx=_ctx
    var data=_data


    class homeViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        var priceview: TextView =itemView.findViewById(R.id.priceview)
        var conditionview: TextView =itemView.findViewById(R.id.conditionview)
        var typeview: TextView =itemView.findViewById(R.id.typeview)
        var mileageview: TextView =itemView.findViewById(R.id.milageview)
        var modelview: TextView =itemView.findViewById(R.id.modelview)
        var manufacturerview: TextView =itemView.findViewById(R.id.manufacturerview)
        var discriptionview: TextView =itemView.findViewById(R.id.discriptionview)
        var imageview: ImageView =itemView.findViewById(R.id.imageview)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): homeViewHolder {

        var itemView=LayoutInflater.from(ctx).inflate(R.layout.homeadd,parent,false)
        return homeAddAdapter.homeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: homeViewHolder, position: Int) {

        holder.priceview.text= data[position].rentingprice
        holder.conditionview.text= data[position].condition
        holder.typeview.text= data[position].type
        holder.mileageview.text= data[position].mileage
        holder.modelview.text= data[position].carModel
        holder.manufacturerview.text= data[position].manufacturer
        holder.discriptionview.text= data[position].description

        if(data[position].images?.get(0).toString() != null){

            val dest = "http://192.168.100.157/hamarisawari/images/" + data[position].images?.get(0).toString()

            //Log.d("My IMG", data.get(position).images?.get(0).toString())
            //Log.d("My position", position.toString())
            ctx?.let { Glide.with(it).load(dest).into(holder.imageview)}
        }


    }

    override fun getItemCount(): Int {
        return data.size

    }

}