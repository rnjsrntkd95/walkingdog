package com.example.walkingdog_kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.walkingdog_kotlin.Post.WritePost
import com.example.walkingdog_kotlin.Walking.StaticsItem
import kotlinx.android.synthetic.main.activity_statics.*
import org.eazegraph.lib.charts.BarChart
import org.eazegraph.lib.models.BarModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class Statics : AppCompatActivity() {

    var month : String? = null
    var day: String? = null
    var kcal = 52.6
    var sum_time:String?=null
    var sum =0

    var now = LocalDate.now()
    var today_month = now.format(DateTimeFormatter.ofPattern("MM"))
    var today_date = now.format(DateTimeFormatter.ofPattern("dd"))
    var yesterday_month =now.minusDays(4)
    var yester=yesterday_month.format(DateTimeFormatter.ofPattern("MM"))


    var history_list = arrayListOf<StaticsItem>(

        StaticsItem("${today_month}/${today_date}","${kcal}", "00","32","22" ),
        StaticsItem("06/22","10","00","20","10")
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statics)

        //프래그먼트에서 상태바 배경색 변경하는 코드
        this.window.statusBarColor = (ContextCompat.getColor(this,
            R.color.green2
        ))

        var sum_kcal=findViewById<TextView>(R.id.sum_kcal_tv)
        var sum_time=findViewById<TextView>(R.id.sum_kcal_tv)

//        for(i in 0..history_list.size){
//            sum+=(history_list[i].cal).toInt()
//        }
//        sum_kcal.text=sum.toString()



        val historyAdapter=Statics_RVAdapter(this, history_list) {staticsItem ->

            //산책 히스토리 리스트 중 한개를 선택했을 때

            finish()    //Statics 액티비티 종료하고

            val intent = Intent(this, WritePost::class.java)    //포스트작성 액티비티 열기
            startActivity(intent)

//            선택한 산책 히스토리의 대한 정보를 변수에 저장 한거임. intent.putExtra로 넘겨주면 될듯
//            var cal = staticsItem.cal
//            var date = staticsItem.date
//            var hour = staticsItem.hour
//            var minutes = staticsItem.minutes
//            var sec = staticsItem.sec


        }

        history_recyclerview.adapter=historyAdapter

        val lm = LinearLayoutManager(this)
        history_recyclerview.layoutManager = lm
        history_recyclerview.setHasFixedSize(true)

        var day_arr = listOf("31","28","31","30","31","30","31","31","30","31","30","31")

        val mBarChart: BarChart = findViewById<View>(R.id.barchart) as BarChart

        var list =arrayListOf<BarModel>(
        )


        for(i in 1..day_arr[today_month.toInt()-1].toInt()) {
            if(i==today_date.toInt()) {
                list.add(BarModel("${i}", "${kcal}".toFloat(), -0xedcbaa))
            } else {
                list.add(BarModel("${i}",0.0f, -0xedcbaa))
            }

        }


        mBarChart.addBarList(list)
        mBarChart.startAnimation()


//        statics_btn.setOnClickListener {
//            Toast.makeText(this,calendar.get(Calendar.DATE),Toast.LENGTH_LONG).show()
//        }
    }

}
