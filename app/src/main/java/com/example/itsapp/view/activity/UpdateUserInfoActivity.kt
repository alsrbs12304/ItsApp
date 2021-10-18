package com.example.itsapp.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.itsapp.R
import kotlinx.android.synthetic.main.activity_update_user_info.*

class UpdateUserInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user_info)
        btnClick()
    }

    fun btnClick(){
        /*툴바 뒤로가기 버튼*/
        back_btn.setOnClickListener {
            this.finish()
        }
        /*중복검사*/
        nick_et.setOnClickListener {

        }
        /*프로필 수정 버튼*/
        profile_btn.setOnClickListener {

        }
        /*수정하기 버튼*/
        update_btn.setOnClickListener {

        }
    }
}