package com.example.walkingdog_kotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.walkingdog_kotlin.model.Challenge


class ChallengeRvAdapter(val context: Context, val challengeList: ArrayList<Challenge>) :
    RecyclerView.Adapter<ChallengeRvAdapter.Holder>() {

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val challengeTitle = itemView?.findViewById<TextView>(R.id.challengeTitle_Textview)

        fun bind (challenge: Challenge, context: Context) {
            challengeTitle?.text = challenge.title
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeRvAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.challenege_rv_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return challengeList.size
    }

    override fun onBindViewHolder(holder: ChallengeRvAdapter.Holder, position: Int) {
        holder?.bind(challengeList[position], context)
    }



}