package com.example.hamarisawari.Admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hamarisawari.R
import com.example.hamarisawari.URLs
import com.example.hamarisawari.vehicles

class AdminAddAdapter(_ctx: Context, _data:ArrayList<vehicles>): RecyclerView.Adapter<AdminAddAdapter.addviewholder>() {
    var ctx=_ctx
    var data=_data



    class addviewholder(itemView: View):RecyclerView.ViewHolder(itemView){

        var nameview: TextView =itemView.findViewById(R.id.anameview)
        var priceview: TextView =itemView.findViewById(R.id.apriceview)
        var conditionview: TextView =itemView.findViewById(R.id.aseatingcapacityview)
        var typeview: TextView =itemView.findViewById(R.id.atypeview)
        var mileageview: TextView =itemView.findViewById(R.id.amilageview)
        var modelview: TextView =itemView.findViewById(R.id.amodelview)
        var manufacturerview: TextView =itemView.findViewById(R.id.amanufacturerview)
        var discriptionview: TextView =itemView.findViewById(R.id.adiscriptionview)
        var numberplateview: TextView =itemView.findViewById(R.id.anumberplateview)
        var enginenumberview: TextView =itemView.findViewById(R.id.aenginenumberview)
        var imageview: ImageView =itemView.findViewById(R.id.aimageview)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): addviewholder {

        var itemView= LayoutInflater.from(ctx).inflate(R.layout.adminaddadapter,parent,false)
        return addviewholder(itemView)
    }

    override fun onBindViewHolder(holder: addviewholder, position: Int) {

        holder.nameview.text= data[position].name
        holder.priceview.text= data[position].rentingprice
        holder.conditionview.text= data[position].seatingCapacity
        holder.typeview.text= data[position].type
        holder.mileageview.text= data[position].mileage
        holder.modelview.text= data[position].carModel
        holder.manufacturerview.text= data[position].manufacturer
        holder.discriptionview.text= data[position].description
        holder.enginenumberview.text=data[position].engineNumber
        holder.numberplateview.text=data[position].numberPlate


        if(data[position].images?.get(0).toString() != null){

            val dest = URLs().images_URL + data[position].images?.get(0).toString()

            //Log.d("My IMG", data.get(position).images?.get(0).toString())
            //Log.d("My position", position.toString())
            ctx.let { Glide.with(it).load(dest).into(holder.imageview)}
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}