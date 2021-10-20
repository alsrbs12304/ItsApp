package com.example.itsapp.view.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.itsapp.view.activity.MyReviewActivity
import com.example.itsapp.R
import com.example.itsapp.view.activity.FavoritesActivity
import com.example.itsapp.view.activity.SplashActivity
import com.example.itsapp.view.activity.UpdateUserInfoActivity
import com.example.itsapp.viewmodel.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_my_page.*
import kotlinx.android.synthetic.main.fragment_my_page.profile_btn

class MyPageFragment : Fragment() {

    private val viewModel:HomeViewModel by viewModels()
    private var userNickname:String=""
    private var userId = ""
    private var loginMethod:String=""
    private var profileUrl:String=""
    private lateinit var selectedImageUri: Uri
    companion object{
        const val TAG : String = "로그"

        fun newInstance() : MyPageFragment{
            return MyPageFragment()
        }
    }

    // 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MyPageFragment -onCreate() called")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "MyPageFragment -onAttach() called")
    }

    // 뷰가 생성됐을 때
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my_page,container,false)
        return view
    }

    override fun onStart() {
        super.onStart()
        loginMethod = viewModel.getLoginMethod()!!
        mypage_login_method.text = loginMethod
        viewModel.userInfo(loginMethod)
        liveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*내정보 수정*/
        update_user_info.setOnClickListener {
            startActivity(Intent(activity,UpdateUserInfoActivity::class.java))
            activity?.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
        /*탈퇴하기 버튼*/
        mypage_retire.setOnClickListener{
            val builder = AlertDialog.Builder(activity)
            builder.setMessage("정말 탈퇴하시겠습니까?")
                .setPositiveButton("탈퇴하기"){dialogInterface, i ->
                    viewModel.retireApp(loginMethod)
                }
                .setNegativeButton("취소"){dialogInterface, i ->

                }
                .show()
        }
        /*문의하기 버튼*/
        inquire_btn.setOnClickListener{
            val url = TalkApiClient.instance.channelChatUrl("_lELGs")
            KakaoCustomTabsClient.openWithDefault(requireContext(),url)
        }
        /*로그아웃 버튼*/
        mypage_logout.setOnClickListener{
            disconnect()
            startActivity(Intent(activity,SplashActivity::class.java))
            activity?.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }

        // 즐겨찾기 (민균)
        go_to_favorites.setOnClickListener {
            startActivity(Intent(activity,FavoritesActivity::class.java))
        }

        // 내 리뷰 (민균)
        my_review_btn.setOnClickListener {
            startActivity(Intent(activity, MyReviewActivity::class.java))
        }
    }
    /*라이브데이터*/
    fun liveData(){
        viewModel.userInfoLiveData.observe(this, Observer {
            userId = it.jsonArray.userId
            userNickname = it.jsonArray.userNickname
            profileUrl =it.jsonArray.profileUrl
            Log.d(TAG, "liveData: $userNickname")
            mypage_nickname.text = userNickname
            Glide.with(requireContext())
                .load(profileUrl)
                .fallback(R.drawable.profile_img)
                .circleCrop()
                .into(profile_btn)
        })
        viewModel.retireLiveData.observe(this, Observer {
            if(it=="200"){
                disconnect()
                val intent = Intent(activity,SplashActivity::class.java)
                startActivity(intent)
                activity?.overridePendingTransition(R.anim.right_in, R.anim.left_out)
                activity?.finish()
            }else {
                Snackbar.make(home_activity, "회원 탈퇴 오류", Snackbar.LENGTH_SHORT).show()
            }
        })
    }
    /*로그아웃,회원탈퇴시 sharedpreference 쿠키 삭제와 카카오 자동로그인 해제 함수*/
    fun disconnect(){
        viewModel.logoutPref()
        if(loginMethod == "카카오"){
            UserApiClient.instance.logout {error ->
                if(error !=null){
                    Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                }
                else {
                    Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                }
            }
        }
    }
}