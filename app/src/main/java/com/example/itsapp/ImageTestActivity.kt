package com.example.itsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.itsapp.viewmodel.DeviceViewModel
import kotlinx.android.synthetic.main.activity_image_test.*

class ImageTestActivity : AppCompatActivity() {

    private val viewModel : DeviceViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_test)

        val deviceName = "Mac Book Pro 16형 Intel Core i9 1TB 용량"
        viewModel.getDeviceInfo(deviceName)
        viewModel.deviceInfoLiveData.observe(this, Observer { deviceInfo ->
            if(deviceInfo.code.equals("200")){
                Log.i("deviceInfo",deviceInfo.toString())
                device_name.text = deviceInfo.jsonArray[0].deviceName
            }
        })
    }
}