package com.example.hamarisawari.Adapters

import android.content.Context
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
import com.example.hamarisawari.Fragments.MoreDetailsFragment
import com.example.hamarisawari.R
import com.example.hamarisawari.URLs
import com.example.hamarisawari.vehicles


class homeAddAdapter(_ctx:Context,_data:ArrayList<vehicles>, _locations:ArrayList<String>): RecyclerView.Adapter<homeAddAdapter.homeViewHolder>() {

    var ctx=_ctx
    var data=_data
    var locations = _locations



    class homeViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        var nameview: TextView =itemView.findViewById(R.id.nameview)
        var priceview: TextView =itemView.findViewById(R.id.priceview)
        var distanceview: TextView =itemView.findViewById(R.id.distanceview)
        //var discriptionview: TextView =itemView.findViewById(R.id.discriptionview)
        var imageview: ImageView =itemView.findViewById(R.id.imageview)
        var btn: Button = itemView.findViewById(R.id.moreDetails)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): homeViewHolder {

        var itemView=LayoutInflater.from(ctx).inflate(R.layout.homeadd,parent,false)


        return homeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: homeViewHolder, position: Int) {

        holder.nameview.text = data[position].name
        holder.priceview.text= data[position].rentingprice
        holder.distanceview.text = locations[position]



        var username = data[position].username
        var numberplate = data[position].numberPlate
        var type = data[position].type

        holder.btn.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val activity = v!!.context as AppCompatActivity


                //activity.supportFragmentManager.beginTransaction().replace(R.id.mainFragment, DetailedFragment).addToBackStack(null).commit()

                val DetailedFragment = MoreDetailsFragment()

                val bundle = Bundle()
                val fragmentTransaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()

                bundle.putString("username", username) // use as per your need
                bundle.putString("numberplate", numberplate)
                bundle.putString("type", type)


                DetailedFragment.arguments = bundle
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.replace(R.id.mainFragment, DetailedFragment)
                fragmentTransaction.commit()
            }
        })
       // holder.discriptionview.text= data[position].description

        if(data[position].images?.get(0).toString() != null){

            val dest = URLs().images_URL + data[position].images?.get(0).toString()

            //Log.d("My IMG", data.get(position).images?.get(0).toString())
            //Log.d("My position", position.toString())
            ctx?.let { Glide.with(it).load(dest).into(holder.imageview)}
        }
    }

    override fun getItemCount(): Int {
        return data.size

    }

}