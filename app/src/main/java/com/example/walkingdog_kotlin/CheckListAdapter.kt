package com.example.walkingdog_kotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CheckListAdapter(val context: Context, val checkItemList : ArrayList<CheckItem>):
        RecyclerView.Adapter<CheckListAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val view = LayoutInflater.from(context!!).inflate(R.layout.check_list_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return checkItemList.size
    }

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        holder?.bind(checkItemList[position], context!!)
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val checkItemName = itemView?.findViewById<CheckBox>(R.id.check_item)
        val testText = itemView?.findViewById<TextView>(R.id.test_text)

        fun bind(checkItem: CheckItem, context: Context) {
            checkItemName?.text = checkItem.item
            testText?.text = checkItem.test
        }
    }
}