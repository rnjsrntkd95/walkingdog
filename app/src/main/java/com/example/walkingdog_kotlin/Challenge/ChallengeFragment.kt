package com.example.walkingdog_kotlin.Challenge

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.walkingdog_kotlin.Challenge.Model.ChallengeListModel
import com.example.walkingdog_kotlin.R
import com.example.walkingdog_kotlin.Challenge.Model.Challenge
import com.example.walkingdog_kotlin.MyChallengeActivity
import com.example.walkingdog_kotlin.Challenge.Model.ChallengeModel
import kotlinx.android.synthetic.main.fragment_challenge.*
import kotlinx.android.synthetic.main.participate_challenge.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChallengeFragment(context: Context) : Fragment() {

    var challengeList = arrayListOf<Challenge>(
    )
    var selected_challenge_title : String? = null
    var selected_challenge_content : String? = null
    var selected_challenge_period : String? = null
    var popularChallengeId:String? =null
    var participateChallengeId : String ? = null

    val pref = context!!.getSharedPreferences("pref", MODE_PRIVATE)
    val userToken = pref.getString("userToken", "5ebac6bd59e3d32080d6d941")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_challenge, container, false)


        //프래그먼트에서 상태바 아이콘 검은색으로 변경하는 코드
        activity!!.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        //프래그먼트에서 상태바 배경색 변경하는 코드
        activity!!.window.statusBarColor = (ContextCompat.getColor(context!!,
            R.color.mainBlue
        ))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var test = challengeTitle_Textview.text

        val cAdapter =
            ChallengeRvAdapter(context!!, challengeList) { challenge ->
                val cDialogView = LayoutInflater.from(context).inflate(R.layout.participate_challenge, null)

                val cBuilder = AlertDialog.Builder(context!!).setView(cDialogView).setTitle("일반 챌린지 등록")

                val cAlertDialog = cBuilder.show()

                cDialogView.select_challenge_title_tv.text = challenge.title
                cDialogView.select_challenge_period_tv.text = challenge.period

                cDialogView.participate_btn.setOnClickListener {
//                    Toast.makeText(context, "${challenge.id}", Toast.LENGTH_LONG).show()
                    val challengeSelectRetrofit = ChallengeRetrofitCreators(context!!).ChallengeRetrofitCreator()
                    challengeSelectRetrofit.joinChallenge(userToken, challenge.id).enqueue(object : Callback<ChallengeListModel> {
                        override fun onFailure(call: Call<ChallengeListModel>, t: Throwable) {
                            Log.d("DEBUG", " Challenge Retrofit failed!!")
                            Log.d("DEBUG", t.message)
                        }
                        override fun onResponse(call: Call<ChallengeListModel>, response: Response<ChallengeListModel>) {
                            Log.d("DEBUG", " Challenge Retrofit Success!!")
                            val error = response.body()?.msg
                            Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
                        }
                    })
                    cAlertDialog.dismiss()
                }


            }
        cRecyclerView.adapter = cAdapter

        val lm = LinearLayoutManager(context)
        cRecyclerView.layoutManager = lm
        cRecyclerView.setHasFixedSize(true)
        //challenges sort by date
        val challengeRetrofit = ChallengeRetrofitCreators(context!!).ChallengeRetrofitCreator()
        challengeRetrofit.getChallengeList(userToken).enqueue(object : Callback<ChallengeListModel> {
            override fun onFailure(call: Call<ChallengeListModel>, t: Throwable) {
                Log.d("DEBUG", " Challenge Retrofit failed!!")
                Log.d("DEBUG", t.message)
            }
            override fun onResponse(call: Call<ChallengeListModel>, response: Response<ChallengeListModel>) {

                val challenges = response.body()?.challenges
                val popularChallenge = response.body()?.popularChallenge

                challenges!!.forEach(fun(element) {
                    Log.d("element.title : ", element.title)
                    challengeList.add(Challenge(element))
                })

                Log.d("Challenge", popularChallenge!!.title)

                challengeTitle_Textview.text = popularChallenge!!.title
                challenge_period.text = popularChallenge!!.start_date.toString() + " ~ " + popularChallenge!!.end_date.toString()

                cAdapter.notifyDataSetChanged()
            }
        })

        create_challenge.setOnClickListener {
            var intent = Intent(context, Create_ChallengeActivity::class.java)
            startActivity(intent)
        }
        // challenge를 누르면 challenge id를 받아옴.
        popular_challenge_content_layout.setOnClickListener {
            val cDialogView = LayoutInflater.from(context).inflate(R.layout.participate_challenge, null)

            val cBuilder = AlertDialog.Builder(context!!).setView(cDialogView).setTitle("챌린지 등록")

            val cAlertDialog = cBuilder.show()

            cDialogView.select_challenge_title_tv.text = challengeTitle_Textview.text
            cDialogView.select_challenge_period_tv.text = challenge_period.text

            cDialogView.participate_btn.setOnClickListener {
                selected_challenge_title = cDialogView.select_challenge_title_tv.text.toString()
                selected_challenge_period = cDialogView.select_challenge_period_tv.text.toString()

                var intent = Intent(context, MyChallengeActivity::class.java)
                startActivity(intent)
                val challengeJoinRetrofit = ChallengeRetrofitCreators(context!!).ChallengeRetrofitCreator()
                challengeJoinRetrofit.joinChallenge(userToken, _id = "가장 사용자가 많은 ChallengeID를 가져와야 ").enqueue(object : Callback<ChallengeListModel> {
                    override fun onFailure(call: Call<ChallengeListModel>, t: Throwable) {
                        Log.d("DEBUG", " Challenge Retrofit failed!!")
                        Log.d("DEBUG", t.message)
                    }
                    override fun onResponse(call: Call<ChallengeListModel>, response: Response<ChallengeListModel>) {
                        Log.d("DEBUG", " Challenge Retrofit Success!!")
                    }
                })

                
                cAlertDialog.dismiss()
            }
        }
    }
}
