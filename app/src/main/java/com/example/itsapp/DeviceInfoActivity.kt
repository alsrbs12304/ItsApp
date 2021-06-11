package com.example.itsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RatingBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.itsapp.model.vo.Device
import com.example.itsapp.model.vo.DeviceInfo
import com.example.itsapp.model.vo.Review
import com.example.itsapp.model.vo.ReviewInfo
import com.example.itsapp.view.adapter.DeviceAdapter
import com.example.itsapp.view.adapter.ReviewAdapter
import com.example.itsapp.viewmodel.DeviceViewModel
import com.example.itsapp.viewmodel.LoginViewModel
import com.example.itsapp.viewmodel.ReviewViewModel
import kotlinx.android.synthetic.main.activity_device_info.*

class DeviceInfoActivity : AppCompatActivity() {

    private val deviewViewModel: DeviceViewModel by viewModels()
    private val reviewViewModel: ReviewViewModel by viewModels()
    val reviewList = arrayListOf<Review>()
    val reviewAdapter = ReviewAdapter(reviewList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_info)

        back_btn.setOnClickListener {
            finish()
        }

        // 디바이스를 선택한 프래그먼트로 부터 deviceName을 넘겨 받아
        // deviceName에 저장한다.
        val intent = intent
        val deviceName = intent.getStringExtra("deviceName")
        Log.i("getString", deviceName.toString())

        deviewViewModel.getDeviceInfo(deviceName!!)
        deviewViewModel.deviceInfoLiveData.observe(this, Observer { deviceInfo ->
            if(deviceInfo.code.equals("200")){
                device_brand.text = deviceInfo.jsonArray[0].deviceBrand
                device_name.text = deviceInfo.jsonArray[0].deviceName
                device_point_avg.text = deviceInfo.jsonArray[0].reviewPoint.toString()
                device_point_avg2.text = deviceInfo.jsonArray[0].reviewPoint.toString()
                device_price.text = deviceInfo.jsonArray[0].devicePrice
                review_count.text = deviceInfo.jsonArray[0].reviewCount.toString()
                rating_bar.rating = deviceInfo.jsonArray[0].reviewPoint.toFloat()
                rating_bar2.rating = deviceInfo.jsonArray[0].reviewPoint.toFloat()
            }
        })
        deviewViewModel.deviceInfoLiveData.observe(this, Observer { deviceInfo ->
            if(deviceInfo.code.equals("200")){
                go_to_review_write_activity.setOnClickListener {
                    val deviceName = deviceInfo.jsonArray[0].deviceName
                    val intent = Intent(this, ReviewWriteActivity::class.java)
                    intent.putExtra("deviceName",deviceName)
                    startActivity(intent)
                }
            }
        })

        rv_review.layoutManager = LinearLayoutManager(this)
        rv_review.adapter = reviewAdapter

        // DeviewInfoActivity에서 보여지는 3개의 리뷰는
        // 리뷰의 좋아요 수가 제일 높은 상위 3개만 보여진다.
        reviewViewModel.getReviewThird(deviceName)
        reviewViewModel.reviewLiveData.observe(this, Observer { reviewInfo ->
            if(reviewInfo.code.equals("200")){
                Log.i("getReview", reviewInfo.jsonArray.toString())
                reviewAdapter.updateItem(reviewInfo.jsonArray)
            }
        })
    }
}