package com.example.itsapp.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.itsapp.R
import com.example.itsapp.model.vo.device.Device
import com.example.itsapp.view.adapter.FavoritesDeviceAdapter
import com.example.itsapp.viewmodel.DeviceViewModel
import com.example.itsapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_device.*

class FavoritesActivity : AppCompatActivity() {

    val deviceList = arrayListOf<Device>()
    val deviceAdapter = FavoritesDeviceAdapter(deviceList)
    private val viewModel: DeviceViewModel by viewModels()
    private val homeViewModel : HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)


        rv_device.layoutManager = GridLayoutManager(this,2)
        rv_device.adapter = deviceAdapter

        val userId = homeViewModel.getLoginSession()
        viewModel.getFavorites(userId)
        viewModel.deviceFavoritesLiveData.observe(this, Observer { deviceInfo ->
            if(deviceInfo.code.equals("200")){
                deviceAdapter.updateItem(deviceInfo.jsonArray)
            }
        })

        deviceAdapter.setItemClickListenerDelete(object : FavoritesDeviceAdapter.OnItemClickListenerDelete{
            override fun onClick(v: View, position: Int) {
                Toast.makeText(applicationContext,"삭제버튼 클릭",Toast.LENGTH_SHORT).show()
            }
        })
        deviceAdapter.setItemClickListenerCompare(object : FavoritesDeviceAdapter.OnItemClickListenerCompare{
            override fun onClick(v: View, position: Int) {
                Toast.makeText(applicationContext,"비교하기 클릭",Toast.LENGTH_SHORT).show()
            }
        })
    }
}