package com.example.walkingdog_kotlin.Walking

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import com.example.walkingdog_kotlin.R
import com.example.walkingdog_kotlin.Walking.Model.SelectDog

class SelectDogAdapter (val context: Context, val selectDogList: ArrayList<SelectDog>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.select_dog_item, null)

        val dogName = view.findViewById<CheckBox>(R.id.select_dog_checkbox)

        val selectDog = selectDogList[position]

        dogName.text = selectDog.name

        return view
    }

    override fun getItem(position: Int): Any {
        return selectDogList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return selectDogList.size
    }

}