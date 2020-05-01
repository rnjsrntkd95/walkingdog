package com.example.walkingdog_kotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.walkingdog_kotlin.model.FeedContent

class FeedAdaptor(val context: Context, val feedList: ArrayList<FeedContent>) :
        RecyclerView.Adapter<FeedAdaptor.Holder>() {

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val feedImg = itemView?.findViewById<ImageView>(R.id.feed_main_img)

        fun bind(feedContent : FeedContent, context: Context) {
            if(feedContent.uploadImg !="") {
                val resourceId = context.resources.getIdentifier(feedContent.uploadImg, "drawable", context.packageName)
                feedImg?.setImageResource(resourceId)
            } else {
                feedImg?.setImageResource(R.mipmap.ic_launcher)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedAdaptor.Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.feed_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return feedList.size

    }

    override fun onBindViewHolder(holder: FeedAdaptor.Holder, position: Int) {
        holder?.bind(feedList[position], context)
    }
}