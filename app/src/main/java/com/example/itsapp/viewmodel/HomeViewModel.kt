package com.example.itsapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.itsapp.model.vo.BrandImage
import com.example.itsapp.model.vo.user.UserInfo
import com.example.itsapp.retrofit.APIInterface
import com.example.itsapp.retrofit.RetrofitClient
import com.example.itsapp.util.SharedPreference
import kotlinx.coroutines.launch

class HomeViewModel(application: Application): AndroidViewModel(application){
    val context = getApplication<Application>().applicationContext
    val prefs:SharedPreference = SharedPreference(context)
    val service: APIInterface = RetrofitClient.getInstance(context).create(
        APIInterface::class.java)
    val userIdLiveData = MutableLiveData<String>()
    val userInfoLiveData = MutableLiveData<UserInfo>()
    val retireLiveData = MutableLiveData<String>()
    val brandImgLiveData = MutableLiveData<BrandImage>()

    fun getLoginSession():String{
        var userSession = ""
        val iterator = prefs.getCookies()?.iterator()
        if(iterator!=null){
            while(iterator.hasNext()){
                userSession =iterator.next()
                userSession = userSession.split(";")[0].split("=")[1]
                Log.d("userInfo", "getLoginSession: $userSession")
            }
        }
        userIdLiveData.postValue(userSession)
        return userSession
    }
    /*유저 정보*/
    fun userInfo(loginMethod:String){
        viewModelScope.launch {
            val data = service.userInfo(loginMethod)
            userInfoLiveData.value = data
        }
    }
    fun getLoginMethod(): String? {
        return prefs.loginMethod
    }
    /*로그아웃시 sharedpreference에서 쿠키 삭제하여 자동 로그인 방지*/
    fun logoutPref(){
        prefs.removeCookies()
        prefs.removeloginMethod()
    }
    /*탈퇴하기 버튼*/
    fun retireApp(loginMethod: String){
        viewModelScope.launch {
            val data = service.retireApp(loginMethod)
            retireLiveData.value = data
        }
    }
    /*브랜드 이미지 가져오기*/
    fun brandImg(){
        viewModelScope.launch {
            val data = service.brandImg()
            brandImgLiveData.value = data
        }
    }
}