package com.example.walkingdog_kotlin

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.walkingdog_kotlin.Challenge.ChallengeRvAdapter
import org.w3c.dom.Text

class PCRvAdapter (val context: Context, val pcList: ArrayList<ParticipateChallenge>) :
    RecyclerView.Adapter<PCRvAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.participatechallenge_rv_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return pcList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(pcList[position], context)
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val rank = itemView?.findViewById<TextView>(R.id.pcRank)
        val nickName = itemView?.findViewById<TextView>(R.id.pcNickname)
        val figure = itemView?.findViewById<TextView>(R.id.pcFigure)
        val satisfy = itemView?.findViewById<TextView>(R.id.pcSatisfy)
        val score = itemView?.findViewById<TextView>(R.id.pcScore)

        fun bind(participateChallenge : ParticipateChallenge, context: Context) {
            if (participateChallenge.score == "0") {
                rank?.text = "-"
            } else {
                rank?.text = participateChallenge.rank
            }
            nickName?.text = participateChallenge.nickName
            figure?.text = participateChallenge.walkingFigure
            satisfy?.text = participateChallenge.walingsatisfy
            score?.text = participateChallenge.score
        }
    }

}