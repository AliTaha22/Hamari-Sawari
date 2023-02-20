package com.example.hamarisawari.Adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.hamarisawari.MyVehiclesClass
import com.example.hamarisawari.R
import com.example.hamarisawari.URLs
import com.example.hamarisawari.ViewMyAd

class myVehiclesAdapter(_ctx:Context,_data:ArrayList<MyVehiclesClass>): RecyclerView.Adapter<myVehiclesAdapter.homeViewHolder>() {

    var ctx=_ctx
    var data=_data
    var seat: Int = 0
    class homeViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        var nameview: TextView =itemView.findViewById(R.id.nameview)
        var imageview: ImageView =itemView.findViewById(R.id.imageview)
        var removeBtn: Button=itemView.findViewById(R.id.removeAd)
        var viewAdBtn: Button=itemView.findViewById(R.id.viewPost)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): homeViewHolder {

        var itemView=LayoutInflater.from(ctx).inflate(R.layout.myvehiclesadapter,parent,false)
        return homeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: homeViewHolder, rawPosition: Int) {

        holder.nameview.text = data[holder.adapterPosition].name
        seat=data[holder.adapterPosition].seating

        holder.removeBtn.setOnClickListener {
        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().removeadd_URL,
            Response.Listener { response ->

                Toast.makeText(ctx, response.toString(), Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { error ->

                Toast.makeText(ctx, error.toString(), Toast.LENGTH_SHORT).show()

            }){
            override fun getParams(): Map<String, String> {
                val map : MutableMap<String,String> = HashMap()
                if(seat > 2)
                {
                    map["numberPlate"]=data[holder.adapterPosition].numberPlate
                    map["table"]="cars"
                }
                else
                {
                    map["numberPlate"]=data[holder.adapterPosition].numberPlate
                    map["table"]="bikes"
                }
                return map }
        }
        val queue = Volley.newRequestQueue(ctx)
        queue.add(request)



        }

        holder.viewAdBtn.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val activity = v!!.context as AppCompatActivity

                val i = Intent(activity, ViewMyAd::class.java)

                val bundle = Bundle()
                bundle.putString("numberplate", data[holder.adapterPosition].numberPlate)

                if(seat > 2)
                {
                    bundle.putString("type", "Car")
                }
                else
                {
                    bundle.putString("type", "Bike")
                }


                i.putExtras(bundle)
                activity.startActivity(i)

            }
        })


        if(data[holder.adapterPosition].image != null){

            val dest = URLs().images_URL + data[holder.adapterPosition].image

            //Log.d("My IMG", data.get(position).images?.get(0).toString())
            //Log.d("My position", position.toString())
            ctx?.let { Glide.with(it).load(dest).into(holder.imageview)}
        }


    }

    override fun getItemCount(): Int {
        return data.size

    }

}