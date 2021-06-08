package com.example.itsapp.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.itsapp.view.adapter.DeviceAdapter
import com.example.itsapp.R
import com.example.itsapp.view.activity.ReviewActivity
import com.example.itsapp.model.vo.Device
import com.example.itsapp.viewmodel.DeviceViewModel
import kotlinx.android.synthetic.main.fragment_apple.*

class AppleFragment : Fragment() {

    val deviceList = arrayListOf<Device>()
    val deviceAdapter = DeviceAdapter(deviceList)
    private val viewModel: DeviceViewModel by viewModels()


    companion object{
        const val TAG : String = "로그"

        fun newInstance() : AppleFragment {
            return AppleFragment()
        }
    }

    // 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    // 뷰가 생성됐을 때
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_apple,container,false)
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
        viewModel.deviceLiveData.observe(viewLifecycleOwner, Observer { deviceInfo ->
            if(deviceInfo.code.equals("200")){
                Log.d("getDeviceInfo Code",deviceInfo.code)
                deviceAdapter.updateItem(deviceInfo.jsonArray)
            }
        })

        deviceAdapter.setItemClickListener(object : DeviceAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                if(position == 0) {
                    val intent = Intent(context, ReviewActivity::class.java)
                    startActivity(intent)
                }
            }
        })
    }
}