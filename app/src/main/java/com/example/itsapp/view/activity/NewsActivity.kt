package com.example.itsapp.view.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.loader.content.CursorLoader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amazonaws.services.s3.AmazonS3Client
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.itsapp.R
import com.example.itsapp.view.adapter.NewsAdapter
import com.example.itsapp.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_news.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class NewsActivity : AppCompatActivity() {
    private val viewModel: NewsViewModel by viewModels()
    private lateinit var newsRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)


        btnEvent()
        rvEvent()
    }
    fun rvEvent(){
        newsRecyclerView = findViewById(R.id.news_rv_activity)
        var NewsLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        newsRecyclerView.layoutManager = NewsLayoutManager
        viewModel.searchReadNews("맥북",1,100)
        viewModel.newsLiveData.observe(this, Observer {
            var result = it.items
            val mAdapter = NewsAdapter(result,this)
            newsRecyclerView.adapter = mAdapter
        })
    }
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

//        upload_img.setOnClickListener {
//            checkPermission()
//        }
    }
//    private fun checkPermission(){
//        when{
//            ContextCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.READ_EXTERNAL_STORAGE
//            )==PackageManager.PERMISSION_GRANTED -> {
//                // 권한이 잘 부여 되었을 때 갤러리에서 사진을 선택하는 기능
//                openAlbum()
//            }
//            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)->{
//                // 교육용 팝 확인 후 권한 팝업 띄우는 기능
//                showContextPopupPermission()
//            }
//            else ->{
//                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
//            }
//        }
//    }
//    private fun openAlbum(){
//        val imageIntent = Intent(Intent.ACTION_PICK)
//        imageIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*")
//        startActivityForResult(imageIntent,SELECT_IMAGE)
//    }
//
//    private fun uploadImage(imageUri:Uri, context:Context) {
//        val image:File = File(getRealPathFromURI(imageUri,context))
//        val requestBody:RequestBody = RequestBody.create(MediaType.parse("image/*"),image)
//
//        val body:MultipartBody.Part = MultipartBody.Part.createFormData("image",image.name,requestBody)
//
//        viewModel.uploadImage(body,"zibro")
//    }
//    private fun LiveData(){
//        viewModel.imgLiveData.observe(this, androidx.lifecycle.Observer {
//            if(it.equals("200")){
//                Snackbar.make(activity_news, "이미지 전송 성공", Snackbar.LENGTH_SHORT).show()
//            }else{
//                Snackbar.make(activity_news, "이미지 전송 실패", Snackbar.LENGTH_SHORT).show()
//            }
//        })
//    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        val selectedImageUri:Uri
//        if(requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null && data.data != null){
//            selectedImageUri = data.data!!
//            uploadImage(selectedImageUri,applicationContext)
//            Log.d("TAG", "onActivityResult: "+selectedImageUri)
//            Glide.with(applicationContext)
//                .load(selectedImageUri)
//                .circleCrop()
//                .into(upload_img)
//        }
//    }
//    private fun getRealPathFromURI(contentUri:Uri,context: Context): String {
//        val proj = arrayOf(MediaStore.Images.Media.DATA)
//        val loader = CursorLoader(context, contentUri, proj, null, null, null)
//        val cursor: Cursor = loader.loadInBackground()!!
//        val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//        cursor.moveToFirst()
//        val result: String = cursor.getString(column_index)
//        cursor.close()
//
//        return result
//    }
}