package com.example.walkingdog_kotlin.Animal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.walkingdog_kotlin.Profile.ProfileItem
import com.example.walkingdog_kotlin.R

class Addpet_RVAdapter (val context: Context, val addpet_list: ArrayList<ProfileItem>):
    RecyclerView.Adapter<Addpet_RVAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.add_dog_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return addpet_list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(addpet_list [position], context)
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        var dogName = itemView?.findViewById<TextView>(R.id.myDogName)


        fun bind (addpet_list: ProfileItem, context: Context) {
            dogName?.text = addpet_list.name
        }
    }

}
