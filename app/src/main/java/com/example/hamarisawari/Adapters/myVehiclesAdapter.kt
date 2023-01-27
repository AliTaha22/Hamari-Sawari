package com.example.hamarisawari.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hamarisawari.MyVehiclesClass
import com.example.hamarisawari.R
import com.example.hamarisawari.URLs
import com.example.hamarisawari.vehicles

class myVehiclesAdapter(_ctx:Context,_data:ArrayList<MyVehiclesClass>): RecyclerView.Adapter<myVehiclesAdapter.homeViewHolder>() {

    var ctx=_ctx
    var data=_data


    class homeViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        var nameview: TextView =itemView.findViewById(R.id.nameview)
        var imageview: ImageView =itemView.findViewById(R.id.imageview)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): homeViewHolder {

        var itemView=LayoutInflater.from(ctx).inflate(R.layout.myvehiclesadapter,parent,false)
        return homeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: homeViewHolder, position: Int) {

        holder.nameview.text = data[position].name

        if(data[position].image != null){

            val dest = URLs().images_URL + data[position].image

            //Log.d("My IMG", data.get(position).images?.get(0).toString())
            //Log.d("My position", position.toString())
            ctx?.let { Glide.with(it).load(dest).into(holder.imageview)}
        }


    }

    override fun getItemCount(): Int {
        return data.size

    }

}