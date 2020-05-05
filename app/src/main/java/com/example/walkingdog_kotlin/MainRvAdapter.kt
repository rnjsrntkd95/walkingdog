package com.example.walkingdog_kotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MainRvAdapter(val context: Context, val dogList: ArrayList<Dog>) :
    RecyclerView.Adapter<MainRvAdapter.Holder>() {

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val dogPhoto = itemView?.findViewById<ImageView>(R.id.dogPhotoImg)
        val dogBreed = itemView?.findViewById<TextView>(R.id.dogBreedTv)
        val dogAge = itemView?.findViewById<TextView>(R.id.dogAgeTv)
        val dogGender = itemView?.findViewById<TextView>(R.id.dogGenderTv)

        fun bind (dog: Dog, context: Context) {
            if (dog.photo != "") {
                val resourceId = context.resources.getIdentifier(dog.photo, "drawable", context.packageName)
                dogPhoto?.setImageResource(resourceId)
            } else {
                dogPhoto?.setImageResource(R.mipmap.ic_launcher)
            }
            dogBreed?.text = dog.breed
            dogAge?.text = dog.age
            dogGender?.text = dog.gender
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.main_rv_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return dogList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(dogList[position], context)
    }
}