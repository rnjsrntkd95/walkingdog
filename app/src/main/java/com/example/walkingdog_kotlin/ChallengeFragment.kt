package com.example.walkingdog_kotlin

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_challenge.*

class ChallengeFragment : Fragment() {

    var challengeList = arrayListOf<Challenge>(
        Challenge("제목1", "challenge01")
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_challenge, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val cAdapter = ChallengeRvAdapter(context!!, challengeList)
        cRecyclerView.adapter = cAdapter

        val lm = LinearLayoutManager(context)
        cRecyclerView.layoutManager = lm
        cRecyclerView.setHasFixedSize(true)


    }

}
