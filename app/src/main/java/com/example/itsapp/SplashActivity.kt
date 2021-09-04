package com.example.itsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import com.example.itsapp.view.activity.HomeActivity
import com.example.itsapp.view.activity.LoadingActivity
import com.example.itsapp.view.activity.MainActivity
import com.example.itsapp.viewmodel.JoinViewModel
import com.kakao.sdk.user.UserApiClient

class SplashActivity : AppCompatActivity() {
    private val viewModel: JoinViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //Handler()가 Deprecated 되어 작업이 자동으로 손실,충돌,경쟁조건이 발생하는 버그 발생 가능
        //때문에 Looper를 넣어서 사용
        Handler(Looper.getMainLooper()).postDelayed({
            /*일반 자동 로그인*/
            AutoLogin()
        },1000)
    }
    /*카카오 자동 로그인*/
    fun kakaoAutoLogin(){
        UserApiClient.instance.me { user, error ->
            if(error !=null){
                Log.e("TAG", "사용자 요청 실패",error )
            }else if(user !=null){
                if(user.kakaoAccount?.email != null){
                    startActivity(Intent(this, HomeActivity::class.java))
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    finish()
                }
            }
        }
    }
    fun AutoLogin(){
        if(!viewModel.getLoginMethod().equals("일반")){
            /*카카오 자동 로그인*/
            kakaoAutoLogin()
        }
        else if(viewModel.getLoginMethod().equals("일반")) {
            val userId = viewModel.getLoginSession()
            if(userId != ""){
                viewModel.setLoginMethod("일반")
                startActivity(Intent(this, LoadingActivity::class.java))
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish()
            }
        }
    }

}