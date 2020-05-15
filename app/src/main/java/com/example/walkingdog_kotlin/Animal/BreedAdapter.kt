package com.example.walkingdog_kotlin.Animal

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.walkingdog_kotlin.Animal.Model.Breed
import com.example.walkingdog_kotlin.R
import kotlinx.android.synthetic.main.breed_rv_item.view.*

class BreedAdapter (val context:Context, val breedList:ArrayList<Breed>, val itemClick: (Breed) -> Unit) :
        RecyclerView.Adapter<BreedAdapter.Holder>() {

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        val breed_button = itemView?.findViewById<ToggleButton>(R.id.breed_btn)
        //val breedText = itemView?.findViewById<TextView>(R.id.breed_tv)

        fun bind(breed: Breed, context: Context){
            breed_button?.text = breed.breed
            breed_button?.textOn = breed.breed
            breed_button?.textOff = breed.breed
            //breedText?.text = breed.breed

            breed_button?.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked) {
                    itemView.setBackgroundResource(R.drawable.male_breed_shape)

                }else {
                    itemView.setBackgroundResource(R.drawable.breed_shape)
                }
            }

            itemView.setOnClickListener {
                itemClick(breed)
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