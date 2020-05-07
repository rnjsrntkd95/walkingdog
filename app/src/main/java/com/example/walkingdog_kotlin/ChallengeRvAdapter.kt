package com.example.walkingdog_kotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChallengeRvAdapter(val context: Context, val challengeList: ArrayList<Challenge>) :
    RecyclerView.Adapter<ChallengeRvAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.challenege_rv_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return challengeList.size
    }

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        holder?.bind(challengeList[position], context)
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val challegePhoto = itemView?.findViewById<ImageView>(R.id.ChallengePhoto_Img)
        val challengeTitle = itemView?.findViewById<TextView>(R.id.challengeTitle_Textview)

        fun bind (challenge: Challenge, context: Context) {
            if (challenge.photo != "") {
                val resourceId = context.resources.getIdentifier(challenge.photo, "drawable", context.packageName)
                challegePhoto?.setImageResource(resourceId)
            } else {
                challegePhoto?.setImageResource(R.mipmap.ic_launcher)
            }
            challengeTitle?.text = challenge.title
        }
    }

}