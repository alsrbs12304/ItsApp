package com.example.itsapp.view.activity

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Base64.NO_WRAP
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itsapp.R
import com.example.itsapp.view.adapter.NewsAdapter
import com.example.itsapp.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.lang.Exception
import java.util.*


class NewsActivity : AppCompatActivity() {
    private val viewModel: NewsViewModel by viewModels()
    private lateinit var newsRecyclerView: RecyclerView
    private val REQUEST_CODE = 1
    private var currentImageURL: Uri? =null
    private var profileImageBase64:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        btnEvent()
        //rvEvent()
        LiveData()
    }
    /*fun rvEvent(){
        newsRecyclerView = findViewById(R.id.news_rv_activity)
        var NewsLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        newsRecyclerView.layoutManager = NewsLayoutManager
        viewModel.searchReadNews("맥북",1,100)
        viewModel.newsLiveData.observe(this, Observer {
            var result = it.items
            val mAdapter = NewsAdapter(result,this)
            newsRecyclerView.adapter = mAdapter
        })
    }*/
    fun btnEvent(){
        back_btn.setOnClickListener{
            finish()
        }

        news_tab.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabSelected(p0: TabLayout.Tab?) {
                val position = p0?.position
                when(position){
                    0 -> {
                        viewModel.searchReadNews("맥북",1,100)
                    }
                    1 -> {
                        viewModel.searchReadNews("삼성 노트북",1,100)
                    }
                    2 -> {
                        viewModel.searchReadNews("LG 노트북",1,100)
                    }
                    3 -> {
                        viewModel.searchReadNews("ASUS 노트북",1,100)
                    }
                    4 -> {
                        viewModel.searchReadNews("DELL 노트북",1,100)
                    }
                    5 -> {
                        viewModel.searchReadNews("LENOVO 노트북",1,100)
                    }
                }
            }
        })

        upload_img.setOnClickListener {
            openGallery()
            //viewModel.uploadImage(profileImageBase64)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)

        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.type = "image/*"
        startActivityForResult(intent,REQUEST_CODE)
    }
    private fun LiveData(){
//        viewModel.imgLiveData.observe(this, Observer {
//            if(it.equals("200")){
//                Snackbar.make(activity_news, "이미지 전송 성공", Snackbar.LENGTH_SHORT).show()
//            }else{
//                Snackbar.make(activity_news, "이미지 전송 실패", Snackbar.LENGTH_SHORT).show()
//            }
//        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            currentImageURL = intent?.data
            val ins: InputStream? = currentImageURL?.let {
                applicationContext.contentResolver.openInputStream(it)
            }
            val img:Bitmap = BitmapFactory.decodeStream(ins)
            ins?.close()
            val resized = Bitmap.createScaledBitmap(img,256,256,true)
            val byteArrayOutputStream = ByteArrayOutputStream()
            resized.compress(Bitmap.CompressFormat.JPEG,60,byteArrayOutputStream)
            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
            profileImageBase64 = Base64.encodeToString(byteArray,NO_WRAP)

            upload_img.setImageURI(currentImageURL)
            try{

            }catch (e:Exception){
                e.printStackTrace()
            }
        }else{
            Log.d("onActivityResult", "onActivityResult: ")
        }
    }
}