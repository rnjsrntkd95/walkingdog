package com.example.walkingdog_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_my_challenge.*

class MyChallengeActivity : AppCompatActivity() {

    var pcList = arrayListOf<ParticipateChallenge>(
        ParticipateChallenge("1", "a", "12", "65", "100"),
        ParticipateChallenge("2", "b", "33", "43", "323"),
        ParticipateChallenge("3", "c", "44", "22", "333"),
        ParticipateChallenge("4", "d", "11", "32", "444"),
        ParticipateChallenge("5", "a", "12", "65", "100"),
        ParticipateChallenge("6", "b", "33", "43", "323"),
        ParticipateChallenge("7", "c", "44", "22", "333")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_challenge)

        val mAdapter = PCRvAdapter(this, pcList)
        participatedChallengeListRecyclerview.adapter = mAdapter

        val lm = LinearLayoutManager(this)
        participatedChallengeListRecyclerview.layoutManager = lm
        participatedChallengeListRecyclerview.setHasFixedSize(true)

        myChallenge_back_btn.setOnClickListener {
            finish()
        }



    }
}
