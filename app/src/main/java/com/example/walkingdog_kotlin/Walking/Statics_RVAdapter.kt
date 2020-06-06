package com.example.walkingdog_kotlin

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.walkingdog_kotlin.Walking.StaticsItem
import com.example.walkingdog_kotlin.model.MyFeedDTO

class Statics_RVAdapter(val context: Context, val history_list: ArrayList<StaticsItem>):
    RecyclerView.Adapter<Statics_RVAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.statics_history_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return history_list.size
    }

    override fun onBindViewHolder(holder: Statics_RVAdapter.Holder, position: Int) {
        holder?.bind(history_list [position], context)
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val history_date = itemView?.findViewById<TextView>(R.id.history_date)
        val history_cal = itemView?.findViewById<TextView>(R.id.history_cal)
        val history_hour = itemView?.findViewById<TextView>(R.id.history_hour)
        val history_minutes = itemView?.findViewById<TextView>(R.id.history_minutes)
        val history_sec = itemView?.findViewById<TextView>(R.id.history_sec)


        fun bind (history_list: StaticsItem, context: Context) {
            /* 나머지 TextView와 String 데이터를 연결한다. */
            history_date?.text = history_list.date
            history_cal?.text = history_list.cal
            history_hour?.text = history_list.hour
            history_minutes?.text = history_list.minutes
            history_sec?.text = history_list.sec
        }
    }
}



