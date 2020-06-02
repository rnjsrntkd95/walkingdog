package com.example.walkingdog_kotlin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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

    var now = LocalDate.now()
    var today_month = now.format(DateTimeFormatter.ofPattern("MM"))
    var today_date = now.format(DateTimeFormatter.ofPattern("dd"))






    var history_list = arrayListOf<StaticsItem>(

        StaticsItem("${today_month}/${today_date}","${kcal}", "00","32","22" )
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statics)



        val historyAdapter=Statics_RVAdapter(this, history_list)
        history_recyclerview.adapter=historyAdapter

        val lm = LinearLayoutManager(this)
        history_recyclerview.layoutManager = lm
        history_recyclerview.setHasFixedSize(true)

        val mBarChart: BarChart = findViewById<View>(R.id.barchart) as BarChart

        var list =arrayListOf<BarModel>(
        )

//        for(j in 0 until history_list.size()){
////        if(today_month!=history_list[j]){
////            list.add(BarModel("${+1}",0.0f, -0xedcbaa)
////        }
////        }

        for (i in 0..30) {

            if (i+1== today_date.toInt()){
                list.add(BarModel("${i+1}", "${kcal}".toFloat(), -0xedcbaa))
            }
            else{
                list.add(BarModel("${i+1}",0.0f, -0xedcbaa))
            }

        }

        mBarChart.addBarList(list)



//        mBarChart.addBar(BarModel("1",30.0f, -0xedcbaa))
//        mBarChart.addBar(BarModel("1",30.0f, -0xedcbaa))
//        mBarChart.addBar(BarModel("1",30.0f, -0xedcbaa))
//        mBarChart.addBar(BarModel("1",30.0f, -0xedcbaa))
//        mBarChart.addBar(BarModel("1",30.0f, -0xedcbaa))


        mBarChart.startAnimation()
//
//        val currentDate = Calendar.getInstance()
//        var y =currentDate.get(Calendar.YEAR) //현재 년도
//        var m =currentDate.get(Calendar.MONTH) // 현재 월(1월 -> 0)
//        var d= currentDate.get(Calendar.DATE)
//
//        val calendar=currentDate.clone() as Calendar
//
//        calendar.add(Calendar.DATE,-2)
//
//        statics_btn.setOnClickListener {
//            Toast.makeText(this,calendar.get(Calendar.DATE),Toast.LENGTH_LONG).show()
//        }
    }

}
