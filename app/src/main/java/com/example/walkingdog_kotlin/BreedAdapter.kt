package com.example.walkingdog_kotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.walkingdog_kotlin.model.Breed

class BreedAdapter (val context:Context, val breedList:ArrayList<Breed>, val itemClick: (Breed) -> Unit) :
        RecyclerView.Adapter<BreedAdapter.Holder>() {

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val breedText = itemView?.findViewById<TextView>(R.id.breed_tv)

        fun bind(breed:Breed, context: Context){
            breedText?.text = breed.breed

            itemView.setOnClickListener { itemClick(breed)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.breed_rv_item, parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return breedList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(breedList[position], context)
    }
}