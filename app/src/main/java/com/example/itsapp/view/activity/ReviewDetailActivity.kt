package com.example.itsapp.view.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.itsapp.viewmodel.CommentViewModel
import com.example.itsapp.R
import com.example.itsapp.model.vo.comment.Comment
import com.example.itsapp.model.vo.review.Review
import com.example.itsapp.model.vo.userDetailInfo
import com.example.itsapp.view.adapter.CommentAdapter
import com.example.itsapp.view.adapter.ReviewAdapter
import com.example.itsapp.viewmodel.DeviceViewModel
import com.example.itsapp.viewmodel.HomeViewModel
import com.example.itsapp.viewmodel.ReviewViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_device_info.*
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.android.synthetic.main.activity_review_detail.*
import kotlinx.android.synthetic.main.activity_review_detail.back_btn
import kotlinx.android.synthetic.main.activity_review_detail.device_brand
import kotlinx.android.synthetic.main.activity_review_detail.device_img
import kotlinx.android.synthetic.main.activity_review_detail.device_name
import kotlinx.android.synthetic.main.activity_review_detail.review_point
import kotlinx.android.synthetic.main.comment_item.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.time.LocalDate

class ReviewDetailActivity : AppCompatActivity() {

    private val commentViewModel : CommentViewModel by viewModels()
    private val reviewViewModel: ReviewViewModel by viewModels()
    private val homeViewModel : HomeViewModel by viewModels()
    private val deviceViewModel: DeviceViewModel by viewModels()
    var commentList = arrayListOf<Comment>()
    val commentAdapter = CommentAdapter(commentList)
    val reviewList = arrayListOf<Review>()
    val reviewAdapter = ReviewAdapter(reviewList)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_detail)

        val userId = homeViewModel.getLoginSession()

        back_btn.setOnClickListener {
            finish()
        }

        val intent = intent
        val deviceName = intent.getStringExtra("deviceName")
        val reviewWriter = intent.getStringExtra("writer") // 리뷰 작성자 닉네임
        go_to_deviceInfo.setOnClickListener {

            val intent = Intent(this, DeviceInfoActivity::class.java)
            intent.putExtra("deviceName",deviceName)
            startActivity(intent)
        }

        reviewViewModel.getChoiceReview(deviceName!!, reviewWriter!!)
        deviceViewModel.choiceDeviceImg(deviceName)
        liveData()
        reviewViewModel.reviewLiveData.observe(this, Observer { reviewInfo ->
            if(reviewInfo.code.equals("200")){
                device_brand.text = reviewInfo.jsonArray[0].deviceBrand
                device_name.text = reviewInfo.jsonArray[0].deviceName
                review_writer.text = reviewInfo.jsonArray[0].writer
                review_point.rating = reviewInfo.jsonArray[0].reviewPoint.toFloat()
                review_write_time.text = reviewInfo.jsonArray[0].writeTime
                content_pros.text = reviewInfo.jsonArray[0].contentPros
                content_cons.text = reviewInfo.jsonArray[0].contentCons
            }
        })
        rv_comment.layoutManager = LinearLayoutManager(this)
        rv_comment.adapter = commentAdapter
        rv_comment.addItemDecoration(DividerItemDecoration(this, 1));

        commentViewModel.getComment(deviceName, reviewWriter)
        commentViewModel.commentLiveData.observe(this, Observer { commentInfo ->
            if(commentInfo.code.equals("200")){
                commentList = commentInfo.jsonArray as ArrayList<Comment>
                commentAdapter.updateItem(commentInfo.jsonArray)
            }
        })

        comment_write_btn.setOnClickListener {
            val commentContent = comment_edt.text.toString()
            if(commentContent.isEmpty()){
                Snackbar.make(review_detail_layout,"댓글을 입력해주세요.", Snackbar.LENGTH_SHORT).show()
            }else{
                commentViewModel.getUserNickName(userId)
                commentViewModel.userNickNameLiveData.observe(this, Observer { userInfo ->
                    if(userInfo.code.equals("200")){
                        val writer = userInfo.jsonArray.userNickname
                        val onlyDate : LocalDate = LocalDate.now()
                        commentViewModel.writeComment(deviceName, reviewWriter, writer, commentContent, onlyDate.toString())
                    }
                })
            }
        }

        commentViewModel.writeCommentLiveData.observe(this, Observer {
            if(it.code.equals("200")){
                hidekeyboard()
                comment_edt.setText(null)
                commentList.add(it.jsonArray[0])
//                commentAdapter.updateItem(commentList)
            }
        })


        commentAdapter.setItemClickListener(object : CommentAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                val builder = AlertDialog.Builder(v.context)
                builder.setTitle("댓글 삭제")
                builder.setMessage("댓글을 삭제하시겠습니까?")
                builder.setPositiveButton("삭제") { dialog: DialogInterface?, which: Int ->
                    commentViewModel.getUserNickName(userId)
                    commentViewModel.userNickNameLiveData.observe(this@ReviewDetailActivity, Observer { userInfo ->
                        if(userInfo.code.equals("200")){
                            Log.i("loginNick",userInfo.jsonArray.userNickname)
                            if(commentList.get(position).writer.equals(userInfo.jsonArray.userNickname)){
                                commentViewModel.deleteComment(commentList.get(position).commnetId, userId)
                                Snackbar.make(review_detail_layout, "댓글이 삭제되었습니다.", Snackbar.LENGTH_SHORT).show()
                                commentAdapter.updateItem2(position)
                            }else{
                                Snackbar.make(review_detail_layout, "댓글 작성자만 해당 댓글을 삭제할 수 있습니다.", Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    })
                }
                builder.setNegativeButton("취소", null)
                builder.show()
            }
        })


        delete_btn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("리뷰 삭제")
            builder.setMessage("리뷰를 삭제하시겠습니까?")
            builder.setPositiveButton("삭제") { dialog: DialogInterface?, which: Int ->
                val loginId = homeViewModel.getLoginSession()
                reviewViewModel.getLoginUserId(loginId)
                reviewViewModel.loginUserIdLiveData.observe(this, Observer { userInfo ->
                    if(userInfo.code.equals("200")) {
                        Log.i("userNickName", userInfo.jsonArray.userNickname)
                        if(reviewWriter.equals(userInfo.jsonArray.userNickname)){
                            reviewViewModel.deleteReview(deviceName,reviewWriter)
                        }else{
                            Toast.makeText(this,"리뷰 작성자만 해당 리뷰를 삭제할 수 있습니다.",Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
            builder.setNegativeButton("취소",null)
            builder.show()
        }
        reviewViewModel.deleteReviewLiveData.observe(this, Observer {
            if(it.equals("200")){
                finish()
                Toast.makeText(this,"리뷰가 삭제되었습니다.",Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DeviceInfoActivity::class.java)
                intent.putExtra("deviceName",deviceName)
                startActivity(intent)
            }
        })
    }
    fun liveData(){
        deviceViewModel.choiceDeviceImgLiveData.observe(this, Observer {
            if(it.code == "200"){
                Glide.with(this)
                    .load(it.jsonArray[0].imgUrl)
                    .into(device_img)
            }else{
                Snackbar.make(home_fragment,"이미지 로드 오류",Snackbar.LENGTH_SHORT).show()
            }
        })
        reviewViewModel.reviewLiveData.observe(this, Observer { reviewInfo ->
            if(reviewInfo.code.equals("200")){
                Glide.with(this)
                    .load(reviewInfo.jsonArray[0].profileUrl).fallback(R.drawable.profile_img).circleCrop()
                    .into(profile_imgd)
            }else{
                Snackbar.make(home_fragment,"이미지 로드 오류",Snackbar.LENGTH_SHORT).show()
            }
        })
    }
    // 키보드 내리기
    fun hidekeyboard(){
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(comment_edt.windowToken,0)
    }
}