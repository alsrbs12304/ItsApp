package com.example.itsapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.itsapp.model.vo.DeviceInfo
import com.example.itsapp.retrofit.APIInterface
import com.example.itsapp.retrofit.RetrofitClient
import com.example.itsapp.util.SharedPreference
import kotlinx.coroutines.launch


class DeviceViewModel(application: Application): AndroidViewModel(application) {
    val context = getApplication<Application>().applicationContext
    val service: APIInterface = RetrofitClient.getInstance(context).create(APIInterface::class.java)
    val prefs = SharedPreference(application)

    val deviceLiveData = MutableLiveData<DeviceInfo>()
    val deviceInfoLiveData = MutableLiveData<DeviceInfo>()

    fun getDevice(deviceBrand : String){
        viewModelScope.launch {
            val data:DeviceInfo = service.getDevice(deviceBrand)
            deviceLiveData.value = data

        }
    }

    fun getDeviceInfo(deviceName : String){
        viewModelScope.launch {
            val data:DeviceInfo = service.getDeviceInfo(deviceName)
            deviceInfoLiveData.value = data
        }
    }
}