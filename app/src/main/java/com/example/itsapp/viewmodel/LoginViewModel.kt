package com.example.itsapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.itsapp.model.vo.user.UserInfo
import com.example.itsapp.retrofit.APIInterface
import com.example.itsapp.retrofit.RetrofitClient
import com.example.itsapp.util.SharedPreference
import kotlinx.coroutines.launch

class LoginViewModel(application: Application): AndroidViewModel(application) {
    val context = getApplication<Application>().applicationContext
    val service: APIInterface = RetrofitClient.getInstance(context).create(
        APIInterface::class.java)
    val prefs = SharedPreference(application)
    val loginLiveData = MutableLiveData<UserInfo>()
    val updatePwLiveData = MutableLiveData<String>()

    /*유저 정보*/
    fun login(userId:String){
        viewModelScope.launch {
            val data = service.login(userId)
            loginLiveData.value = data
        }
    }
    fun updatePw(userId:String,userPw:String){
        viewModelScope.launch {
            val data = service.updatePw(userId,userPw)
            updatePwLiveData.value = data
        }
    }
    fun setLoginMethod(value:String){
        prefs.loginMethod = value
    }
}