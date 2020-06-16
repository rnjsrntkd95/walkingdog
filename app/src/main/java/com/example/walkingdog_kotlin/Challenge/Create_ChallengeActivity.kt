package com.example.walkingdog_kotlin.Challenge

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.example.walkingdog_kotlin.Challenge.Model.ChallengeModel
import com.example.walkingdog_kotlin.Challenge.Model.CreateChallengeResultModel
import com.example.walkingdog_kotlin.MyChallengeActivity
import com.example.walkingdog_kotlin.R
import kotlinx.android.synthetic.main.activity_create__challenge.*
import kotlinx.android.synthetic.main.fragment_challenge.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Create_ChallengeActivity() : AppCompatActivity() {

    var now = LocalDate.now()
    var month = now.format(DateTimeFormatter.ofPattern("MM"))
    var date = now.format(DateTimeFormatter.ofPattern("dd"))
    var todayMonth: String? = null
    var todayDate: String? = null
    var test: String? = null
    var selectBreed: String? = null

    val dateList = listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create__challenge)

        createChallenge_back_btn.setOnClickListener {
            finish()
        }

        current_month_tv.text = Integer.parseInt(month).toString()
        current_date_tv.text = Integer.parseInt(date).toString()
        todayMonth = current_month_tv.text.toString()
        todayDate = current_date_tv.text.toString()

        period_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var index = (Integer.parseInt(todayMonth)-1)

                when(period_spinner.getItemAtPosition(position)) {
                    "3일" -> {
                        target_month_tv.text = Integer.parseInt(todayMonth).toString()
                        target_date_tv.text = (Integer.parseInt(todayDate)+2).toString()

                        if((Integer.parseInt(todayDate)+2) > dateList.get(index)) {
                            target_month_tv.text = ((Integer.parseInt(todayMonth)) +1).toString()
                            target_date_tv.text = ((Integer.parseInt(todayDate)+2)-dateList.get(index)).toString()
                        }
                    }
                    "7일" -> {

                        target_month_tv.text = Integer.parseInt(todayMonth).toString()
                        target_date_tv.text = (Integer.parseInt(todayDate)+6).toString()

                        if((Integer.parseInt(todayDate)+6) > dateList.get(index)) {
                            target_month_tv.text = ((Integer.parseInt(todayMonth)) +1).toString()
                            target_date_tv.text = ((Integer.parseInt(todayDate)+6)-dateList.get(index)).toString()
                        }
                    }
                    "15일" -> {
                        target_month_tv.text = Integer.parseInt(todayMonth).toString()
                        target_date_tv.text = (Integer.parseInt(todayDate)+14).toString()

                        if((Integer.parseInt(todayDate)+14) > dateList.get(index)) {
                            target_month_tv.text = ((Integer.parseInt(todayMonth)) +1).toString()
                            target_date_tv.text = ((Integer.parseInt(todayDate)+14)-dateList.get(index)).toString()
                        }
                    }
                    "30일" -> {
                        target_month_tv.text = Integer.parseInt(todayMonth).toString()
                        target_date_tv.text = (Integer.parseInt(todayDate)+29).toString()

                        if((Integer.parseInt(todayDate)+29) > dateList.get(index)) {
                            target_month_tv.text = ((Integer.parseInt(todayMonth)) +1).toString()
                            target_date_tv.text = ((Integer.parseInt(todayDate)+29)-dateList.get(index)).toString()
                        }
                    }
                }
            }

        }
        breed_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                create_challenge_breed_tv.text = breed_spinner.getItemAtPosition(position).toString()

            }

        }


        createChallenge_complete_btn.setOnClickListener {
            var title = challenge_title_edit_tv.text.toString()
            var content = challenge_content_tv.text.toString()
            var breed = create_challenge_breed_tv.text.toString()
            var targetMonth = if(target_month_tv.text.toString().length == 1) "0"+target_month_tv.text.toString() else target_month_tv.text.toString()
            var targetDate = if(target_date_tv.text.toString().length == 1) "0"+target_date_tv.text.toString() else target_date_tv.text.toString()
            var today = LocalDate.parse(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            var target = LocalDate.parse(now.format(DateTimeFormatter.ofPattern("yyyy")).toString() + "-" + targetMonth + "-" + targetDate)

            val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
            val userToken = pref.getString("userToken", "")

            val challengeCreateRetrofit = ChallengeRetrofitCreators(this).ChallengeRetrofitCreator()
             challengeCreateRetrofit.createChallenge(title, content, breed, today, target, userToken).enqueue(object : Callback<CreateChallengeResultModel> {
                override fun onFailure(call: Call<CreateChallengeResultModel>, t: Throwable) {
                    Log.d("DEBUG", "Create Challenge Retrofit Failed!!")
                    Log.d("DEBUG", t.message)
                }
                override fun onResponse(
                    call: Call<CreateChallengeResultModel>,
                    response: Response<CreateChallengeResultModel>
                ) {
                    Log.d("DEBUG", "Create Challenge Retrofit Success!!")
                    val challengeId = response.body()?.challengeId
                    val error = response.body()?.error
                    Log.d("ERROR", error.toString())
                    if (challengeId == null || challengeId =="") {
                        return
                    }
                    val intent = Intent(this@Create_ChallengeActivity, MyChallengeActivity::class.java)
                    intent.putExtra("challengeId", challengeId)
                    startActivity(intent)
                    finish()
                }
            })

            finish()
        }
    }
}
