package com.example.walkingdog_kotlin.Walking

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.walkingdog_kotlin.Post.WritePost
import com.example.walkingdog_kotlin.R
import com.example.walkingdog_kotlin.ReformatDate
import com.example.walkingdog_kotlin.Statics_RVAdapter
import com.example.walkingdog_kotlin.Walking.Model.MyWalkingStaticModel
import kotlinx.android.synthetic.main.activity_statics.*
import org.eazegraph.lib.charts.BarChart
import org.eazegraph.lib.models.BarModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class Statics : AppCompatActivity() {
    var now = LocalDate.now()
    var staticMonth: String = ""

    var month : String? = null
    var day: String? = null
    var kcal = 52.6
    var sum_time:String?=null
    var sum =0

    var today_month = now.format(DateTimeFormatter.ofPattern("M"))
    var today_date = now.format(DateTimeFormatter.ofPattern("dd"))
    var yesterday_month =now.minusDays(4)
    var yester=yesterday_month.format(DateTimeFormatter.ofPattern("MM"))


    var day_arr = listOf("31","28","31","30","31","30","31","31","30","31","30","31")
    var chartList =arrayListOf<BarModel>()
    var history_list = arrayListOf<StaticsItem>()
    var chartColor = listOf("#92CFBF", "#C7E2CF", "#E68A7B", "#D9B191")






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statics)

        val mBarChart: BarChart = findViewById<View>(R.id.barchart) as BarChart
        var sum_kcal=findViewById<TextView>(R.id.sum_kcal_tv)
        var sum_time=findViewById<TextView>(R.id.sum_time_tv)

        //프래그먼트에서 상태바 배경색 변경하는 코드
        this.window.statusBarColor = (ContextCompat.getColor(this,
            R.color.green2
        ))

        val historyAdapter= Statics_RVAdapter(this, history_list) { staticsItem ->
            //산책 히스토리 리스트 중 한개를 선택했을 때
            finish()    //Statics 액티비티 종료하고

            val intent = Intent(this, WritePost::class.java)    //포스트작성 액티비티 열기
            intent.putExtra("walkingId", staticsItem.walkingId)
            startActivity(intent)
        }


        history_recyclerview.adapter=historyAdapter
        val lm = LinearLayoutManager(this)
        history_recyclerview.layoutManager = lm
        history_recyclerview.setHasFixedSize(true)


        // 산책 통계 기록 요청
        val pref = getSharedPreferences("pref", MODE_PRIVATE)
        val userToken = pref.getString("userToken", "")
        val walkingRetrofit = WalkingRetrofitCreators(this).WalkingRetrofitCreator()
        walkingRetrofit.getMyWalkingStatic(userToken!!).enqueue(object :
            Callback<MyWalkingStaticModel> {
            override fun onFailure(call: Call<MyWalkingStaticModel>, t: Throwable) {
                Log.d("DEBUG", " Static Retrofit failed!!")
                Log.d("DEBUG", t.message)
                Toast.makeText(this@Statics, "산책 통계 로딩 실패. 네트워크를 확인해주세요.",
                    Toast.LENGTH_LONG
                ).show()
            }
            override fun onResponse(call: Call<MyWalkingStaticModel>, response: Response<MyWalkingStaticModel>) {
                val error = response.body()?.error
                val walkings = response.body()?.walkings
                Log.d("DEBUG", error.toString())

                walkings!!.forEach(fun(walking) {
                    val month = ReformatDate("MM", walking.date)
                    val day = ReformatDate("dd", walking.date)
                    if (staticMonth == "") {
                        staticMonth = month
                        chartList.clear()
                        for(i in 1..day_arr[staticMonth.toInt()-1].toInt()) {
                            chartList.add(BarModel("${i}일", 0F,
                                Color.parseColor(chartColor[day.toInt() % chartColor.size])))
                        }
                        staticMonth_view.text = "${month.toInt()}"
                    }

                    val time = walking.walkingTime
                    val hour = (time / 144000) % 24 // 1시간
                    val min = (time / 6000) % 60 // 1분
                    val sec = (time / 100) % 60 // 1초
                    var sHour: String = "--"
                    var sMin: String = "--"
                    var sSec: String = "--"

                    if (hour < 10) { // 시
                        sHour = "0$hour"
                    } else {
                        sHour = "$hour"
                    }

                    if (min < 10) { // 분
                        sMin = "0$min"
                    } else {
                        sMin = "$min"
                    }

                    if (sec < 10) {
                        sSec = "0$sec"
                    } else {
                        sSec = "$sec"
                    }

                    history_list.add(StaticsItem("$month/$day", String.format("%.1f", walking.calories.toFloat()),
                        sHour, sMin, sSec, walking._id))

                    chartList[day.toInt()-1] = BarModel("${day.toInt()}일",
                        walking.calories.toFloat(), Color.parseColor(chartColor[day.toInt() % chartColor.size]))
                })

                historyAdapter.notifyDataSetChanged()

                // Bar 차트 Setting
                mBarChart.addBarList(chartList)
                mBarChart.barWidth = 50f
                mBarChart.barMargin = 10F
                mBarChart.animationTime = 1000
                mBarChart.startAnimation()
            }
        })









//        for(i in 0..history_list.size){
//            sum+=(history_list[i].cal).toInt()
//        }
//        sum_kcal.text=sum.toString()











//        statics_btn.setOnClickListener {
//            Toast.makeText(this,calendar.get(Calendar.DATE),Toast.LENGTH_LONG).show()
//        }
    }

}
