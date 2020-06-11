package com.example.walkingdog_kotlin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.walkingdog_kotlin.Challenge.ChallengeRetrofitCreators
import com.example.walkingdog_kotlin.Challenge.Model.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_my_challenge.*
import kotlinx.android.synthetic.main.fragment_challenge.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyChallengeActivity : AppCompatActivity() {

    var pcList = arrayListOf<ParticipateChallenge>(
        ParticipateChallenge("1", "김현진", "30", "100%", "100"),
        ParticipateChallenge("2", "권구상", "28", "90%", "95"),
        ParticipateChallenge("3", "김민정", "25", "85%", "90"),
        ParticipateChallenge("4", "박준성", "10", "40%", "60"),
        ParticipateChallenge("5", "이수만", "5", "30%", "40"),
        ParticipateChallenge("6", "박진영", "3", "10%", "20")

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_challenge)

        this.window.statusBarColor = (ContextCompat.getColor(this,
            R.color.green2
        ))

        val mAdapter = PCRvAdapter(this, pcList)
        participatedChallengeListRecyclerview.adapter = mAdapter

        val lm = LinearLayoutManager(this)
        participatedChallengeListRecyclerview.layoutManager = lm
        participatedChallengeListRecyclerview.setHasFixedSize(true)

        // 챌린지 유저 가져오기
        val pref = this.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val userToken = pref.getString("userToken", "")
        var challengeId = ""
        val extra = intent.extras
        if (extra != null) {
            challengeId = intent.getStringExtra("challengeId")
            if (challengeId == null || challengeId == "") {
                finish()
                bottom_navigation.selectedItemId = R.id.action_challenge
            }
        }

        val challengeDetailRetrofit = ChallengeRetrofitCreators(this).ChallengeRetrofitCreator()
        challengeDetailRetrofit.getUserList(userToken!!, challengeId).enqueue(object : Callback<ChallengeUserListModel> {
            override fun onFailure(call: Call<ChallengeUserListModel>, t: Throwable) {
                Log.d("DEBUG", " Challenge Retrofit failed!!")
                Log.d("DEBUG", t.message)
            }
            override fun onResponse(call: Call<ChallengeUserListModel>, response: Response<ChallengeUserListModel>) {
                val records = response.body()?.records
                val myRecord = response.body()?.myRecord

                if (myRecord != null) {
                    //가입 중인 챌린지가 있다면
                    myRecordLayout.visibility = View.VISIBLE
                    joinChallenge.visibility = View.GONE
                } else {
                    //가입 중인 챌린지가 없다면
                    myRecordLayout.visibility = View.GONE
                    cancelChallenge.visibility = View.GONE
                }

                var rankCounter = 1
                var myrank = 0
                // 전체 기록들 반영
                records!!.forEach(fun(record) {
                    pcList.add(ParticipateChallenge("${rankCounter}", record.user.nickname,
                        "${record.walkingCount}", "${record.walkingAvg}", "${record.score}"))
                    if (record.user.nickname == myRecord!!.user.nickname) {
                        myrank = rankCounter
                    }
                    rankCounter++
                })
                mAdapter.notifyDataSetChanged()

                // 내 기록 반영
                rankingFigureTv.text = myrank.toString()
                walkingFigureTv.text = myRecord!!.walkingCount.toString()
                satisfiedQuantityFigureTv.text = myRecord.walkingAvg.toString()
                scoreFigureTv.text = myRecord.score.toString()

                if(pcList.size > 3){
                    firstPerson.text = pcList[0].nickName
                    secondPerson.text = pcList[1].nickName
                    thirdPerson.text = pcList[2].nickName
                }

            }
        })
        myChallenge_back_btn.setOnClickListener {
            finish()
        }

        cancelChallenge.setOnClickListener{
            val challengeCancelRetrofit = ChallengeRetrofitCreators(this).ChallengeRetrofitCreator()
            challengeCancelRetrofit.deleteChallenge(userToken, challengeId).enqueue(object : Callback<DropChallengeResultModel> {
                override fun onFailure(call: Call<DropChallengeResultModel>, t: Throwable) {
                    Log.d("DEBUG", "Drop Challenge Retrofit failed!!")
                    Log.d("DEBUG", t.message)
                }
                override fun onResponse(call: Call<DropChallengeResultModel>, response: Response<DropChallengeResultModel>) {
                    Log.d("DEBUG", "Drop Challenge Retrofit SUCCESS!!")
                    val error = response.body()?.error
                    if (error == 1) {
                        Toast.makeText(this@MyChallengeActivity, "챌린지 탈퇴 실패하였습니다.", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@MyChallengeActivity, "챌린지 탈퇴하였습니다.", Toast.LENGTH_LONG).show()
                    }
                }
            })
            finish()
        }

        joinChallenge.setOnClickListener {
            val challengeJoinRetrofit = ChallengeRetrofitCreators(this).ChallengeRetrofitCreator()
            challengeJoinRetrofit.joinChallenge(userToken, challengeId).enqueue(object : Callback<JoinChallengeResultModel> {
                override fun onFailure(call: Call<JoinChallengeResultModel>, t: Throwable) {
                    Log.d("DEBUG", "Join Challenge Retrofit failed!!")
                    Log.d("DEBUG", t.message)
                }
                override fun onResponse(call: Call<JoinChallengeResultModel>, response: Response<JoinChallengeResultModel>) {
                    Log.d("DEBUG", "JoinChallenge Retrofit SUCCESS!!")
                    val error = response.body()?.error
                    if (error == 2) {
                        Toast.makeText(this@MyChallengeActivity, "이미 가입된 챌린지가 존재합니다.", Toast.LENGTH_LONG).show()
                    } else if (error == 1) {
                        Toast.makeText(this@MyChallengeActivity, "챌린지 가입에 실패하였습니다.", Toast.LENGTH_LONG).show()
                    }

                    val intent = Intent(this@MyChallengeActivity, MyChallengeActivity::class.java)
                    intent.putExtra("challengeId", challengeId)
                    startActivity(intent)
                    finish()
                }
            })
        }
    }
}
