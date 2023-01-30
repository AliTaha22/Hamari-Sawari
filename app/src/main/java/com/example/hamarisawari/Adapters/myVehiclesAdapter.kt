package com.example.hamarisawari.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.hamarisawari.MyVehiclesClass
import com.example.hamarisawari.R
import com.example.hamarisawari.URLs
import com.example.hamarisawari.vehicles
import org.json.JSONArray

class myVehiclesAdapter(_ctx:Context,_data:ArrayList<MyVehiclesClass>): RecyclerView.Adapter<myVehiclesAdapter.homeViewHolder>() {

    var ctx=_ctx
    var data=_data
    var seat=""
    class homeViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        var nameview: TextView =itemView.findViewById(R.id.nameview)
        var imageview: ImageView =itemView.findViewById(R.id.imageview)
        var removeBtn: Button=itemView.findViewById(R.id.removeAd)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): homeViewHolder {

        var itemView=LayoutInflater.from(ctx).inflate(R.layout.myvehiclesadapter,parent,false)
        return homeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: homeViewHolder, position: Int) {

        holder.nameview.text = data[position].name
        seat=data[position].seating.toString()
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
                if(seat> 2.toString())
                {
                    map["numberPlate"]=data[holder.adapterPosition].numberPlate.toString()
                    map["table"]="cars"
                }
                else
                {
                    map["numberPlate"]=data[holder.adapterPosition].numberPlate.toString()
                    map["table"]="bikes"
                }
                return map }
        }
        val queue = Volley.newRequestQueue(ctx)
        queue.add(request)



        }
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