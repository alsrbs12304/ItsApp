package com.example.itsapp.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.itsapp.R
import com.example.itsapp.model.vo.review.Review
import com.example.itsapp.view.adapter.ReviewAdapter
import com.example.itsapp.viewmodel.HomeViewModel
import com.example.itsapp.viewmodel.ReviewViewModel
import kotlinx.android.synthetic.main.activity_my_review.*
import kotlinx.android.synthetic.main.activity_review.*

class MyReviewActivity : AppCompatActivity() {

    private val homeViewModel : HomeViewModel by viewModels()
    private val reviewViewModel : ReviewViewModel by viewModels()
    val reviewList = arrayListOf<Review>()
    val reviewAdapter = ReviewAdapter(reviewList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_review)

        val userId = homeViewModel.getLoginSession()

        my_review_rv.layoutManager = LinearLayoutManager(this)
        my_review_rv.adapter = reviewAdapter
        my_review_rv.addItemDecoration(DividerItemDecoration(this, 1));

        reviewViewModel.getMyReview(userId)
        reviewViewModel.MyReviewLiveData.observe(this, Observer { reviewInfo ->
            if(reviewInfo.code.equals("200")) {
                Log.i("getMyReview", reviewInfo.jsonArray.toString())
                reviewAdapter.updateItem(reviewInfo.jsonArray)
            }
        })


        reviewViewModel.MyReviewLiveData.observe(this, Observer { reviewInfo ->
            if(reviewInfo.code.equals("200")){
                reviewAdapter.setItemClickListener(object : ReviewAdapter.OnItemClickListener{
                    override fun onClick(v: View, position: Int) {
                        val deviceName = reviewInfo.jsonArray[position].deviceName
                        val writer = reviewInfo.jsonArray[position].writer
                        val intent = Intent(application, ReviewDetailActivity::class.java)
                        intent.putExtra("deviceName", deviceName)
                        intent.putExtra("writer", writer)
                        startActivity(intent)
                    }
                })
            }
        })
    }
}