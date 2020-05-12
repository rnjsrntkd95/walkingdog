package com.example.walkingdog_kotlin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.walkingdog_kotlin.model.Challenge
import kotlinx.android.synthetic.main.fragment_challenge.*

class ChallengeFragment : Fragment() {

    var challengeList = arrayListOf<Challenge>(
        Challenge("제목1"),
        Challenge("제목2"),
        Challenge("제목3"),
        Challenge("제목4"),
        Challenge("제목5"),
        Challenge("제목6"),
        Challenge("제목7"),
        Challenge("제목8"),
        Challenge("제목9")
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
