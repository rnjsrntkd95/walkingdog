package com.example.walkingdog_kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.eazegraph.lib.charts.ValueLineChart
import org.eazegraph.lib.models.ValueLinePoint

import org.eazegraph.lib.models.ValueLineSeries





class Statics : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statics)

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
