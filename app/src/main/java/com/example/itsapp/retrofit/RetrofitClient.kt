package com.example.itsapp.retrofit

import android.content.Context
import com.example.itsapp.util.AddCookiesInterceptor
import com.example.itsapp.util.ReceivedCookiesInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private var instance: Retrofit? = null

    fun getInstance(context: Context):Retrofit{
        if(instance == null){
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val okHttpClient = OkHttpClient.Builder()
                .addNetworkInterceptor(AddCookiesInterceptor(context))
                .addNetworkInterceptor(ReceivedCookiesInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES) // 요청을 시작한 후 서버와의 TCP handshake가 완료되기 까지 지속되는 시간.
                .readTimeout(30, TimeUnit.SECONDS) // 읽기 시간 초과는 연결이 설정되면 모든 바이트가 전송되는 속도를 감시한다.
                .writeTimeout(15, TimeUnit.SECONDS) // 쓰기 타임 아웃은 읽기 타움 아웃의 반대 방향이다. 얼마나 빨리 서버에 바이트를 보낼 수 있는지 확인한다.
                .build()
            instance = Retrofit.Builder()
                .baseUrl("http://ec2-54-180-29-97.ap-northeast-2.compute.amazonaws.com:3000")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
        }
        return instance!!
   }
}