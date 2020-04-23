package com.example.walkingdog_kotlin

import com.example.walkingdog_kotlin.BuildConfig.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitCreator {

    companion object {
        val SERVER_BASE_URL = "127.0.0.1:3000"

        private fun ServerRetrofit(): Retrofit {
            return Retrofit.Builder().
                baseUrl(SERVER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(createOkHttpClient())
                .build()
        }

        fun <T> create(service: Class<T>): T {
            return ServerRetrofit().create(service)
        }

        private fun createOkHttpClient(): OkHttpClient {
            val intercepter = HttpLoggingInterceptor()

            if (DEBUG) {
                intercepter.level = HttpLoggingInterceptor.Level.BODY
            } else {
                intercepter.level = HttpLoggingInterceptor.Level.NONE
            }

            return OkHttpClient.Builder()
                .addNetworkInterceptor(intercepter)
                .build()
        }
    }
}