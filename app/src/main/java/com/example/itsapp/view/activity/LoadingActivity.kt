package com.example.itsapp.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.itsapp.R
import com.example.itsapp.viewmodel.HomeViewModel

class LoadingActivity : AppCompatActivity() {
    private val viewmodel:HomeViewModel by viewModels()
    companion object{
        lateinit var id:String
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        viewmodel.getLoginSession()
        liveData()
    }
    private fun liveData(){
        viewmodel.userIdLiveData.observe(this, Observer { userId ->
            Log.d("TAG", "onCreate: $userId")
            id = userId
            if(userId!=""){
                viewmodel.seceondJoin(userId)
            }else {
                viewmodel.getLoginSession()
            }
        })
        viewmodel.secondJoinLiveData.observe(this, Observer { code ->
            if(code.equals("200")){
                startActivity(Intent(this, HomeActivity::class.java))
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish()
            }else if (code.equals("204")) {
                val intent = Intent(this, AddUserInfoActivity::class.java)
                intent.putExtra("userId",id)
                startActivity(intent)
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish()
            }
        })
    }

}