package com.example.walkingdog_kotlin

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.walkingdog_kotlin.model.ServerApiModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("TAG", "test------------")
        var SERVER_BASE_URL = "http://172.30.73.147:3000"

        var retrofit = Retrofit.Builder()
            .baseUrl(SERVER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        Log.d("TAG", "성공~~")

        var getName = retrofit.create(ServerApiRouter::class.java)
        Log.d("TAG", "성공~~")

        getName.getPostAppName().enqueue(object: Callback<ServerApiModel> {
            override fun onFailure(call: Call<ServerApiModel>, t: Throwable) {
                //통신실패
                Log.d("DEBUG", "실패")

                Log.d("DEBUG", t.message)
            }

            override fun onResponse(call: Call<ServerApiModel>, response: Response<ServerApiModel>) {
                //통신성공
                Log.d("DEBUG", ")()()()()()()()()()()()")
                var appName = response.body()
                Log.d("DEBUG", appName?.appname)
            }

        })



    }
}
