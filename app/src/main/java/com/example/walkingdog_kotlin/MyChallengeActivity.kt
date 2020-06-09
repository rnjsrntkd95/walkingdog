package com.example.walkingdog_kotlin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.walkingdog_kotlin.Challenge.ChallengeRetrofitCreators
import com.example.walkingdog_kotlin.Challenge.Model.Challenge
import com.example.walkingdog_kotlin.Challenge.Model.ChallengeListModel
import com.example.walkingdog_kotlin.Challenge.Model.ChallengeUserListModel
import kotlinx.android.synthetic.main.activity_my_challenge.*
import kotlinx.android.synthetic.main.fragment_challenge.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyChallengeActivity : AppCompatActivity() {

    var pcList = arrayListOf<ParticipateChallenge>(
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

//        챌린지 유저 가져오기
        val pref = this.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val userToken = pref.getString("userToken", "5ebac6bd59e3d32080d6d941")
        val challengeId = intent.getStringExtra("challengeId")
        var code = 0

        val challengeDetailRetrofit = ChallengeRetrofitCreators(this).ChallengeRetrofitCreator()
        challengeDetailRetrofit.getUserList(userToken, challengeId).enqueue(object : Callback<ChallengeUserListModel> {
            override fun onFailure(call: Call<ChallengeUserListModel>, t: Throwable) {
                Log.d("DEBUG", " Challenge Retrofit failed!!")
                Log.d("DEBUG", t.message)
            }
            override fun onResponse(call: Call<ChallengeUserListModel>, response: Response<ChallengeUserListModel>) {
                val users = response.body()?.challengeUserList
                val myChallenge = response.body()?.myChallenge
                var rankCounter = 1
                var myrank = 0
                users!!.forEach(fun(element) {
                    pcList.add(ParticipateChallenge(rankCounter.toString(), element.userId.nickname, element.walkingCount.toString(), element.walkingAvg.toString(), element.score.toString()))
                    if(element.userId._id.equals(userToken)){
                        myrank = rankCounter
                    }
                    rankCounter++
                    mAdapter.notifyDataSetChanged()
                })
                rankingFigureTv.text = myrank.toString()
                walkingFigureTv.text = myChallenge!![0].walkingCount.toString()
                satisfiedQuantityFigureTv.text = myChallenge!![0].walkingAvg.toString()
                scoreFigureTv.text = myChallenge!![0].score.toString()

                if(pcList.size > 3){
                    firstPerson.text = pcList[0].nickName
                    secondPerson.text = pcList[1].nickName
                    thirdPerson.text = pcList[2].nickName
                }
                //리스트가 바로 업데이트 되도록 추가해야함
            }
        })
        myChallenge_back_btn.setOnClickListener {
            finish()
        }

        cancelChallenge.setOnClickListener{
            val challengeCancelRetrofit = ChallengeRetrofitCreators(this).ChallengeRetrofitCreator()
            challengeCancelRetrofit.deleteChallenge(userToken, _id = challengeId).enqueue(object : Callback<ChallengeListModel> {
                override fun onFailure(call: Call<ChallengeListModel>, t: Throwable) {
                    Log.d("DEBUG", " Challenge Retrofit failed!!")
                    Log.d("DEBUG", t.message)
                }
                override fun onResponse(call: Call<ChallengeListModel>, response: Response<ChallengeListModel>) {
                    Log.d("DEBUG", " Challenge Retrofit SUCCESS!!")
                    var msg = response.body()?.msg.toString()
                    Toast.makeText(this@MyChallengeActivity, "$msg", Toast.LENGTH_LONG).show()
                }
            })
            finish()
        }
        var flag = true
        code = intent.getIntExtra("flag", 0)
        Log.d("codecode", code.toString())
        if(code != 0) flag = false
        if(flag) {      //가입 중인 챌린지가 있다면
            myRecordLayout.visibility = View.VISIBLE
        } else {         //가입 중인 챌린지가 없다면
            //나의 기록을 보여주지 않는다.
            myRecordLayout.visibility = View.GONE
        }

    }
}
