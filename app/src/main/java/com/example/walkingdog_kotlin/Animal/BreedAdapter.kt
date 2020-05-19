package com.example.walkingdog_kotlin.Animal

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.walkingdog_kotlin.Animal.Model.Breed
import com.example.walkingdog_kotlin.R
import kotlinx.android.synthetic.main.breed_rv_item.view.*

class BreedAdapter (val context:Context, val breedList:ArrayList<Breed>, var mCallBack: View.OnClickListener ,val itemClick: (Breed) -> Unit) :
        RecyclerView.Adapter<BreedAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.breed_rv_item, parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return breedList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(breedList[position], context)

        if(breedList?.get(position) is Breed) {
            val dataItem = breedList.get(position) as Breed
            if (dataItem.isSelected) {
                context?.let { ContextCompat.getColor(it, R.color.mainOrange) }
                    ?.let { holder.itemView.setBackgroundResource(R.drawable.male_breed_shape) }

            } else {
                context?.let { ContextCompat.getColor(it, R.color.gray) }
                    ?.let { holder.itemView.setBackgroundResource(R.drawable.breed_shape) }

            }
        }

    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        var breedText = itemView?.findViewById<TextView>(R.id.breed_tv)

        fun bind(breed: Breed, context: Context){
            breedText?.text = breed.breed


            itemView.setOnClickListener {
                itemClick(breed)

                val list = breedList as List<Breed>
                for(item in list.indices) {
                    list[item].isSelected = false
                }
                list[adapterPosition].isSelected = true

                mCallBack.onClick(itemView)
                notifyDataSetChanged()

                context?.let { it1 -> ContextCompat.getColor(it1, R.color.mainOrange) }?.let { it2 ->
                    itemView.setBackgroundColor(it2)
                }
            }
        }
    }



}