package com.example.itsapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.itsapp.model.vo.BrandImage
import com.example.itsapp.model.vo.DeviceImage
import com.example.itsapp.model.vo.device.DeviceInfo
import com.example.itsapp.model.vo.favorites.FavoritesInfo
import com.example.itsapp.model.vo.spec.SpecInfo
import com.example.itsapp.retrofit.APIInterface
import com.example.itsapp.retrofit.RetrofitClient
import com.example.itsapp.util.SharedPreference
import kotlinx.coroutines.launch


class DeviceViewModel(application: Application): AndroidViewModel(application) {
    val context = getApplication<Application>().applicationContext
    val service: APIInterface = RetrofitClient.getInstance(context).create(APIInterface::class.java)

    val deviceLiveData = MutableLiveData<DeviceInfo>()
    val deviceInfoLiveData = MutableLiveData<DeviceInfo>()
    val deviceSpecLiveData = MutableLiveData<DeviceInfo>()
    val deviceAddFavoritesLiveData = MutableLiveData<FavoritesInfo>()
    val deviceFavoritesLiveData = MutableLiveData<DeviceInfo>()
    val deviceImgLiveData = MutableLiveData<DeviceImage>()
    val deviceDeleteFavoritesLiveData = MutableLiveData<String>()

    val choiceDeviceImgLiveData = MutableLiveData<DeviceImage>()

    fun getDevice(deviceBrand : String){
        viewModelScope.launch {
            val data: DeviceInfo = service.getDevice(deviceBrand)
            deviceLiveData.value = data

        }
    }

    fun getDeviceInfo(deviceName : String){
        viewModelScope.launch {
            val data: DeviceInfo = service.getDeviceInfo(deviceName)
            deviceInfoLiveData.value = data
        }
    }

    fun getSpec(deviceName: String){
        viewModelScope.launch {
            val data: DeviceInfo = service.getSpec(deviceName)
            deviceSpecLiveData.value = data
        }
    }

    fun addFavorites(deviceName: String, userId:String){
        viewModelScope.launch {
            val data: FavoritesInfo = service.addFavorites(deviceName,userId)
            deviceAddFavoritesLiveData.value = data
        }
    }
    fun getFavorites(userId : String){
        viewModelScope.launch {
            val data:DeviceInfo = service.getFavorites(userId)
            deviceFavoritesLiveData.value = data
        }
    }
    fun deleteFavorites(userId: String,deviceName: String){
        viewModelScope.launch {
            val data:String = service.deleteFavorites(userId, deviceName)
            deviceDeleteFavoritesLiveData.value = data
        }
    }
    fun deviceImg(){
        viewModelScope.launch {
            val data = service.deviceImg()
            deviceImgLiveData.value = data
        }
    }
    fun choiceDeviceImg(deviceName: String){
        viewModelScope.launch {
            val data = service.choiceDeviceImg(deviceName)
            choiceDeviceImgLiveData.value = data
        }
    }
}