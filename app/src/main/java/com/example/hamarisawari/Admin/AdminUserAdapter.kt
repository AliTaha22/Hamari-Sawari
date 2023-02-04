package com.example.hamarisawari.Admin

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import com.example.hamarisawari.AdminUserB
import com.example.hamarisawari.R
import com.example.hamarisawari.URLs
import com.example.hamarisawari.users

class AdminUserAdapter(_ctx: Context, _data:ArrayList<users>,_check:Int):RecyclerView.Adapter<AdminUserAdapter.userviewholder>() {
    var ctx=_ctx
    var data=_data
    var check=_check
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
        return userviewholder(itemView)
    }

    override fun onBindViewHolder(holder: userviewholder, position: Int) {
        holder.nameview.text=data[position].name
        holder.ageview.text=data[position].age
        holder.genderview.text=data[position].gender
        holder.contactview.text=data[position].contact
        holder.usernameview.text=data[position].username
        holder.idview.text=data[position].id
        holder.ratingview.text=data[position].rating

        if(check==1)
        {
            holder.blockbtn.text="Unblock"
        }
        holder.blockbtn.setOnClickListener {

            val request: StringRequest = object : StringRequest(
                Method.POST, URLs().blockunblock_URL,
                Response.Listener { response ->

                    Toast.makeText(ctx, response.toString(), Toast.LENGTH_SHORT).show()
                    if(check==0) {
                        ctx.startActivity(Intent(ctx, AdminUser::class.java))
                        (ctx as Activity).finish()
                    }
                    else{
                        ctx.startActivity(Intent(ctx, AdminUserB::class.java))
                        (ctx as Activity).finish()
                    }
                },
                Response.ErrorListener { error ->

                    Toast.makeText(ctx, error.toString(), Toast.LENGTH_SHORT).show()

                }) {
                override fun getParams(): Map<String, String> {
                    val map: MutableMap<String, String> = HashMap()
                    map["username"]=data[holder.adapterPosition].username.toString()
                    if(check==0) {
                        map["check"] = 0.toString()
                    }
                    else{
                        map["check"] = 1.toString()
                    }
                    return map
                }
            }
            val queue = Volley.newRequestQueue(ctx)
            queue.add(request)
        }

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