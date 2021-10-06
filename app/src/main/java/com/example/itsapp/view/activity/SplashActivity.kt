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
import com.example.itsapp.viewmodel.SplashViewModel
import com.kakao.sdk.user.UserApiClient

class SplashActivity : AppCompatActivity() {
    private val viewModel: SplashViewModel by viewModels()
    companion object{
        lateinit var id:String
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val intent = getIntent()
        val flag = intent.getStringExtra("from")
        Log.d("TAG", "onCreate: $flag")
        if(flag.isNullOrEmpty()){
            //Handler()가 Deprecated 되어 작업이 자동으로 손실,충돌,경쟁조건이 발생하는 버그 발생 가능
            //때문에 Looper를 넣어서 사용
            Log.d("today", "onCreate: flag null")
            Handler(Looper.getMainLooper()).postDelayed({
                /*일반 자동 로그인*/
                AutoLogin()
            },1000)
        }else
            checkPrefs()
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
        Log.d("today", "onCreate: flag null kakao login")
        if(viewModel.getLoginMethod().equals("카카오")) {
            Log.d("today", "onCreate: flag null kakao auto login")
            /*카카오 자동 로그인*/
            kakaoAutoLogin()
        } else if (viewModel.getLoginMethod().equals("일반")) {
            Log.d("today", "onCreate: flag null general login")
            val userId = viewModel.getLoginSession()
            if (userId.isNotBlank()) {
                Log.d("today", "onCreate: flag null userid is null")
                viewModel.setLoginMethod("일반")
                checkPrefs()
            }
        }else{
            Log.d("today", "onCreate: flag null go to main")
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            finish()
        }
    }

    fun checkPrefs(){ //looadingActivity 대체
        Log.d("today", "onCreate: checkPrefs")
        viewModel.getLoginSession()
        viewModel.userIdLiveData.observe(this, Observer {
            Log.d("TAG", "checkPrefs: $it")
            id = it
            if(id.isNotBlank())
                viewModel.secondJoin(id)
            else
                viewModel.getLoginSession()
        })
        viewModel.secondJoinLiveData.observe(this, Observer {code ->
            if(code.equals("200")){
                startActivity(Intent(this, HomeActivity::class.java))
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish()
            }else if (code.equals("204")) {
                val intent = Intent(this, AddUserInfoActivity::class.java)
                intent.putExtra("userId", id)
                startActivity(intent)
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish()
            }
        })
    }

}