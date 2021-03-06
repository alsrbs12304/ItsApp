package com.example.itsapp.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.itsapp.view.adapter.DeviceAdapter
import com.example.itsapp.R
import com.example.itsapp.view.activity.DeviceInfoActivity
import com.example.itsapp.model.vo.device.Device
import com.example.itsapp.viewmodel.DeviceViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_device_info.*
import kotlinx.android.synthetic.main.fragment_device.*
import kotlinx.android.synthetic.main.fragment_device.device_brand
import kotlinx.android.synthetic.main.fragment_home.*


class DeviceFragment : Fragment() {

    val deviceList = arrayListOf<Device>()
    val deviceAdapter = DeviceAdapter(deviceList)
    private val viewModel: DeviceViewModel by viewModels()
    companion object{
        const val TAG : String = "로그"

        fun newInstance() : DeviceFragment {
            return DeviceFragment()
        }
    }

    // 뷰가 생성됐을 때
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_device,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_device.layoutManager = GridLayoutManager(activity,2)
        rv_device.adapter = deviceAdapter

        // HomeFragment에서 클릭된 카드뷰 정보(어느 브랜드 인지)를 받아온다. (HomeFragment <--> Fragment(this)
            // HomeFragment에서 bundle 객체를 전달받아 그 값을 변수(deviceBrand)에 저장한다.
        // 서버에 브랜드 정보를 보내고 그 브랜드에 맞는 디바이스 정보를 받아와서 (Fragment <--> 서버)
            // deviceBrand 값을 서버에 전송해서 해당 브랜드 맞는 디바이스 정보를 가져와 화면에 뿌려준다.
        val deviceBrand = arguments?.getString("deviceBrand")
        device_brand.text = deviceBrand
        viewModel.getDevice(deviceBrand!!)

        viewModel.deviceImg()
//        liveData()

        viewModel.deviceLiveData.observe(viewLifecycleOwner, Observer { deviceInfo ->
            if(deviceInfo.code.equals("200")){
                deviceAdapter.updateItem(deviceInfo.jsonArray)
            }
        })

        deviceAdapter.setItemClickListener(object : DeviceAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                // 클릭된 deviceName을 DeviceInfoActivity로 넘겨줘야 한다.
                // 액티비티로 넘겨준 deviceName으로 어떤 제품을 불러올 것인지를 체크하기 위함
                // 액티비티로 넘겨 주는 거기 때문에 bundle이 아니라 intent로 넘긴다.
                // 프래그먼트로 넘길 시엔 bundle 사용
                var deviceName = deviceAdapter.deviceList[position].deviceName
                val intent = Intent(activity, DeviceInfoActivity::class.java)
                intent.putExtra("deviceName",deviceName)
                startActivity(intent)
            }
        })
    }
    fun liveData(){
        viewModel.deviceImgLiveData.observe(viewLifecycleOwner, Observer {
            if(it.code == "200"){

            }else{
                Snackbar.make(home_fragment,"이미지 로드 오류", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

}