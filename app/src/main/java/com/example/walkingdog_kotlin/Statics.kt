package com.example.walkingdog_kotlin

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.walkingdog_kotlin.Walking.StaticsItem
import kotlinx.android.synthetic.main.activity_statics.*
import org.eazegraph.lib.charts.BarChart
import org.eazegraph.lib.models.BarModel


class Statics : AppCompatActivity() {

    var history_list = arrayListOf<StaticsItem>(

        StaticsItem("Today","26.4", "00","32","22" )
        ,StaticsItem("Yesterday","34.7","00","41","10")
        , StaticsItem("2 days ago","50.1","01","05","36")
        ,StaticsItem("3 days ago","42.3","00","56","45")
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


        mBarChart.addBar(BarModel(2.3f, -0xedcbaa))
        mBarChart.addBar(BarModel(2f, -0xcbcbaa))
        mBarChart.addBar(BarModel(3.3f, -0xa9cbaa))
        mBarChart.addBar(BarModel(1.1f, -0x78c0aa))
        mBarChart.addBar(BarModel(2.7f, -0xa9480f))
        mBarChart.addBar(BarModel(2f, -0xcbcbaa))
        mBarChart.addBar(BarModel(0.4f, -0xe00b54))
        mBarChart.addBar(BarModel(4f, -0xe45b1a))

        mBarChart.startAnimation()


    }

}
