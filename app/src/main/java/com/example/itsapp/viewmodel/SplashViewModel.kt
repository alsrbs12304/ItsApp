package com.example.itsapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.itsapp.retrofit.APIInterface
import com.example.itsapp.retrofit.RetrofitClient
import com.example.itsapp.util.SharedPreference
import kotlinx.coroutines.launch

class SplashViewModel(application: Application):AndroidViewModel(application) {
    val context = getApplication<Application>().applicationContext
    val prefs: SharedPreference = SharedPreference(context)
    val service: APIInterface = RetrofitClient.getInstance(context).create(
        APIInterface::class.java)
    val userIdLiveData = MutableLiveData<String>()
    val secondJoinLiveData = MutableLiveData<String>()


    //로그인 경험 유무
    fun getLoginSession():String{
        var userSession = ""
        val iterator = prefs.getCookies()?.iterator()
        if(iterator!=null){
            while(iterator.hasNext()){
                userSession =iterator.next()
                userSession = userSession.split(";")[0].split("=")[1]
                Log.d("userInfo", "getLoginSession: $userSession")
            }
        }else if(iterator==null){
            userIdLiveData.postValue(userSession)
            return userSession
        }
        userIdLiveData.postValue(userSession)
        return userSession
    }
    //ㅇ
    fun setLoginMethod(value:String){
        prefs.loginMethod = value
    }
    //o
    fun getLoginMethod(): String? {
        return prefs.loginMethod
    }
    fun secondJoin(userId:String){
        viewModelScope.launch {
            val data = service.seceondJoin(userId)
            secondJoinLiveData.value = data
        }
    }
}