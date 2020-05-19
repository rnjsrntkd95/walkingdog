package com.example.walkingdog_kotlin.Post

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.walkingdog_kotlin.R
import com.example.walkingdog_kotlin.model.FeedContent

class FeedAdaptor(val context: Context, val feedList: ArrayList<FeedContent>, val itemClick: (FeedContent) -> Unit) :
        RecyclerView.Adapter<FeedAdaptor.Holder>() {

    inner class Holder(itemView: View?, itemClick: (FeedContent) -> Unit) : RecyclerView.ViewHolder(itemView!!) {
        val profileImg = itemView?.findViewById<ImageView>(R.id.myfeed_profile_img)
        val userName = itemView?.findViewById<TextView>(R.id.myfeed_nickname)
        val date = itemView?.findViewById<TextView>(R.id.dateTv)
        val location = itemView?.findViewById<TextView>(R.id.myfeed_locate_text)
        val dogName = itemView?.findViewById<TextView>(R.id.dogNameTv)
        val feedImg = itemView?.findViewById<ImageView>(R.id.feed_main_img)
        val explain = itemView?.findViewById<TextView>(R.id.explain_text)
        val walkingTime = itemView?.findViewById<TextView>(R.id.walkingTimeTv)
        val walkingDistance = itemView?.findViewById<TextView>(R.id.walkingDistanceTv)
        val walkingCal = itemView?.findViewById<TextView>(R.id.walkingCalory)
        val likes = itemView?.findViewById<TextView>(R.id.likesTv)


        fun bind(feedContent : FeedContent, context: Context) {
//            if(feedContent.profileImg !="") {
//                val resourceId = context.resources.getIdentifier(feedContent.profileImg, "drawable", context.packageName)
//                profileImg?.setImageResource(resourceId)
//            } else {
//                profileImg?.setImageResource(R.mipmap.ic_launcher)
//            }

            userName?.text = feedContent.userName
            date?.text = feedContent.date.toString()
            location?.text = feedContent.location
            dogName?.text = feedContent.dogName

            if(feedContent.uploadImg !="") {
                val resourceId = context.resources.getIdentifier(feedContent.uploadImg, "drawable", context.packageName)
                feedImg?.setImageResource(resourceId)
            } else {
                feedImg?.setImageResource(R.mipmap.ic_launcher)
            }

            explain?.text = feedContent.explain
            walkingTime?.text = feedContent.time.toString()
            walkingDistance?.text = feedContent.distance.toString()
            walkingCal?.text = feedContent.calory.toString()
            likes?.text = feedContent.likes.toString()

            itemView.setOnClickListener { itemClick(feedContent) }


        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.feed_item, parent, false)
        return Holder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return feedList.size

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(feedList[position], context)
    }
}