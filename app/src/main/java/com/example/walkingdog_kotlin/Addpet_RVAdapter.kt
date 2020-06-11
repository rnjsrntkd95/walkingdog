package com.example.walkingdog_kotlin

import android.content.Context
import android.os.Parcel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.walkingdog_kotlin.ProfileItem

class Addpet_RVAdapter (val context: Context, val addpet_list: ArrayList<ProfileItem>):
    RecyclerView.Adapter<Addpet_RVAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.add_dog_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return addpet_list.size
    }

    override fun onBindViewHolder(holder: Addpet_RVAdapter.Holder, position: Int) {
        holder?.bind(addpet_list [position], context)
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val pet_img = itemView?.findViewById<ImageView>(R.id.add_dog_img)



        fun bind (addpet_list: ProfileItem, context: Context) {

            if (addpet_list.image != "") {
                val resourceId = context.resources.getIdentifier(addpet_list.image, "drawable", context.packageName)
                pet_img?.setImageResource(resourceId)
            } else {
                pet_img?.setImageResource(R.mipmap.ic_launcher)
            }



        }
    }

}
