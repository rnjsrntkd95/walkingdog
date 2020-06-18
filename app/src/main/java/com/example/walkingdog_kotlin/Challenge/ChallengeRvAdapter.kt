package com.example.walkingdog_kotlin.Challenge

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.walkingdog_kotlin.R
import com.example.walkingdog_kotlin.Challenge.Model.Challenge


class ChallengeRvAdapter(val context: Context, val challengeList: ArrayList<Challenge>, val itemClick: (Challenge) -> Unit) :
    RecyclerView.Adapter<ChallengeRvAdapter.Holder>() {

    inner class Holder(itemView: View?, itemClick: (Challenge) -> Unit) : RecyclerView.ViewHolder(itemView!!) {
        val challengeTitle = itemView?.findViewById<TextView>(R.id.challengeTitle_Textview)
        val challengeComment = itemView?.findViewById<TextView>(R.id.challengeComment_Textview)
        val challengePeriod = itemView?.findViewById<TextView>(R.id.challengePeriod_Textview)
        val challengeNumber = itemView?.findViewById<TextView>(R.id.challengeNumber_Textview)



        fun bind (challenge: Challenge, context: Context) {
            challengeTitle?.text = challenge.title
            challengeComment?.text = challenge.comment
            challengePeriod?.text = challenge.period
            challengeNumber?.text = challenge.number

            itemView.setOnClickListener { itemClick(challenge) }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.challenege_rv_item, parent, false)
        return Holder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return challengeList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(challengeList[position], context)
    }



}