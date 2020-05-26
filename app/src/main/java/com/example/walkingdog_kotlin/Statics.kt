package com.example.walkingdog_kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.walkingdog_kotlin.Walking.StaticsItem
import com.example.walkingdog_kotlin.Statics_RVAdapter
import kotlinx.android.synthetic.main.activity_my_photo.*
import kotlinx.android.synthetic.main.activity_statics.*
import org.eazegraph.lib.charts.ValueLineChart
import org.eazegraph.lib.models.ValueLinePoint
import org.eazegraph.lib.models.ValueLineSeries





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

        val mCubicValueLineChart =
            findViewById(R.id.linechart) as ValueLineChart

        val series = ValueLineSeries()
        series.color = -0xa9480f

        series.addPoint(ValueLinePoint("Jan", 2.4f))
        series.addPoint(ValueLinePoint("Feb", 3.4f))
        series.addPoint(ValueLinePoint("Mar", .4f))
        series.addPoint(ValueLinePoint("Apr", 1.2f))
        series.addPoint(ValueLinePoint("Mai", 2.6f))
        series.addPoint(ValueLinePoint("Jun", 1.0f))
        series.addPoint(ValueLinePoint("Jul", 3.5f))
        series.addPoint(ValueLinePoint("Aug", 2.4f))
        series.addPoint(ValueLinePoint("Sep", 2.4f))
        series.addPoint(ValueLinePoint("Oct", 3.4f))
        series.addPoint(ValueLinePoint("Nov", .4f))
        series.addPoint(ValueLinePoint("Dec", 1.3f))

        mCubicValueLineChart.addSeries(series)
        mCubicValueLineChart.startAnimation()


    }

}
