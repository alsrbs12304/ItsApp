package com.example.itsapp.view.activity

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.itsapp.R
import com.example.itsapp.model.vo.device.Device
import com.example.itsapp.model.vo.spec.Spec
import com.example.itsapp.view.adapter.FavoritesDeviceAdapter
import com.example.itsapp.viewmodel.DeviceViewModel
import com.example.itsapp.viewmodel.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.android.synthetic.main.activity_home.*


class FavoritesActivity : AppCompatActivity() {

    val deviceList = arrayListOf<Device>()
    val specList = arrayListOf<Spec>()
    val deviceAdapter = FavoritesDeviceAdapter(deviceList)
    private val viewModel: DeviceViewModel by viewModels()
    private val homeViewModel : HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        rv_favorites_device.layoutManager = LinearLayoutManager(this)
        rv_favorites_device.addItemDecoration(deviceAdapter.VerticalItemDecorator(10))
        rv_favorites_device.adapter = deviceAdapter

        val userId = homeViewModel.getLoginSession()
        viewModel.getFavorites(userId)
        viewModel.deviceImg()
        viewModel.deviceFavoritesLiveData.observe(this, Observer { deviceInfo ->
            if(deviceInfo.code.equals("200")){
                deviceAdapter.updateItem(deviceInfo.jsonArray)
                empty_txt.visibility = View.INVISIBLE
            }else if(deviceInfo.code.equals("204")){
                empty_txt.visibility = View.VISIBLE
            }
        })



        // ???????????? ?????? ?????? ?????? ???
        deviceAdapter.setItemClickListenerDelete(object : FavoritesDeviceAdapter.OnItemClickListenerDelete{
            override fun onClick(v: View, position: Int) {
                val builder = AlertDialog.Builder(v.context)
                builder.setTitle("???????????? ??????")
                builder.setMessage("?????????????????? ?????????????????????????")
                builder.setPositiveButton("??????") { dialog: DialogInterface?, which: Int ->
                    var deviceName = deviceAdapter.deviceList[position].deviceName
                    viewModel.deleteFavorites(userId,deviceName)
                    deviceAdapter.updateItem2(position)
                    finish() //????????? ??????
                    overridePendingTransition(0, 0) //????????? ?????? ?????????
                    val intent = getIntent() //?????????
                    startActivity(intent) //???????????? ??????
                    overridePendingTransition(0, 0) //????????? ?????? ?????????
                }
                builder.setNegativeButton("??????",null)
                builder.show()
            }
        })

        viewModel.deviceDeleteFavoritesLiveData.observe(this, Observer {
            if(it.equals("200")){
                Snackbar.make(favorites_activity, "?????????????????? ?????????????????????..", Snackbar.LENGTH_SHORT).show()
            }
        })
    }
}