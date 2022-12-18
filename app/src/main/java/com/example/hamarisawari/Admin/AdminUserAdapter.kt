package com.example.hamarisawari

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AdminUserAdapter(_ctx: Context, _data:ArrayList<users>):RecyclerView.Adapter<AdminUserAdapter.userviewholder>() {
    var ctx=_ctx
    var data=_data


    class userviewholder(itemview:View):RecyclerView.ViewHolder(itemview){

        var nameview:TextView=itemview.findViewById(R.id.unameview)
        var ageview:TextView=itemview.findViewById(R.id.uageview)
        var genderview:TextView=itemview.findViewById(R.id.ugenderview)
        var contactview:TextView=itemview.findViewById(R.id.ucontactview)
        var usernameview:TextView=itemview.findViewById(R.id.uusernameview)
        var idview:TextView=itemview.findViewById(R.id.uidview)
        var ratingview:TextView=itemview.findViewById(R.id.uratingview)
        var imageview:ImageView=itemview.findViewById(R.id.uimageview)
        var blockbtn:Button=itemview.findViewById(R.id.ublock)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): userviewholder {
        var itemView= LayoutInflater.from(ctx).inflate(R.layout.adminuseradapter,parent,false)
        return AdminUserAdapter.userviewholder(itemView)
    }

    override fun onBindViewHolder(holder: userviewholder, position: Int) {
        holder.nameview.text=data[position].name
        holder.ageview.text=data[position].age
        holder.genderview.text=data[position].gender
        holder.contactview.text=data[position].contact
        holder.usernameview.text=data[position].username
        holder.idview.text=data[position].id
        holder.ratingview.text=data[position].rating

        if(data[position].images.toString() != null){

            val dest = "http://192.168.100.157/hamarisawari/images/" + data[position].images.toString()

            //Log.d("My IMG", data.get(position).images?.get(0).toString())
            //Log.d("My position", position.toString())
            ctx?.let { Glide.with(it).load(dest).into(holder.imageview)}
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}