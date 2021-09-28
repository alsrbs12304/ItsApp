package com.example.itsapp.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.itsapp.R
import com.example.itsapp.viewmodel.JoinViewModel
import com.kakao.sdk.user.UserApiClient

class SplashActivity : AppCompatActivity() {
    private val viewModel: JoinViewModel by viewModels()
    companion object{
        lateinit var id:String
    }
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
                startActivity(Intent(this, MainActivity::class.java))
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish()
            }else if(user !=null){
                if(user.kakaoAccount?.email != null){
                    viewModel.setLoginMethod("카카오")
                    startActivity(Intent(this, HomeActivity::class.java))
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    finish()
                }
            }
        }
    }
    fun AutoLogin(){
        if(viewModel.getLoginMethod().equals("카카오")) {
            /*카카오 자동 로그인*/
            kakaoAutoLogin()
        } else if (viewModel.getLoginMethod().equals("일반")) {
            val userId = viewModel.getLoginSession()
            if (userId != "") {
                viewModel.setLoginMethod("일반")
                startActivity(Intent(this, LoadingActivity::class.java))
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish()
            }
        }else{
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            finish()
        }
    }

    fun checkPrefs(){
        viewModel.getLoginSession()
        viewModel.userIdLiveData.observe(this, Observer {
            Log.d("TAG", "checkPrefs: $it")
            id = it
            if(id.isNotBlank())
                //viewModel.checkIdLiveData
            else
                viewModel.getLoginSession()
        })
    }

}