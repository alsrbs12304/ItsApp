package com.example.itsapp.view.activity

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.example.itsapp.R
import com.example.itsapp.viewmodel.JoinViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_add_user_info.*
import kotlinx.android.synthetic.main.activity_add_user_info.profile_btn
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AddUserInfoActivity : AppCompatActivity() {

    private var checkNick = false
    private var checkProfile = false
    private val viewModel:JoinViewModel by viewModels()
    private lateinit var selectedImageUri: Uri
    private val getContent: ActivityResultLauncher<Intent> = //
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null && it.data?.data != null) {
                selectedImageUri = it.data?.data!!
                Log.d("TAG", "onActivityResult: $selectedImageUri")
                Glide.with(applicationContext)
                    .load(selectedImageUri)
                    .fallback(R.drawable.profile_img)
                    .circleCrop()
                    .into(profile_btn)
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user_info)

        eventBtn()
        liveData()
    }
    private fun eventBtn(){
        profile_btn.setOnClickListener {
            checkProfile = true
            checkPermission()
        }
        kakao_nick_check_btn.setOnClickListener {
            val nickname = kakao_nick_et.text.toString().trim()
            viewModel.checkNick(nickname)
        }

        kakaoId_update_btn.setOnClickListener {
            val id = intent.getStringExtra("userId")
            val nickname = kakao_nick_et.text.toString().trim()
            if(id!=null&&checkNick){
                if(checkProfile){
                    val image = File(getRealPathFromURI(selectedImageUri, applicationContext))
                    val requestBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), image)

                    val body: MultipartBody.Part =
                        MultipartBody.Part.createFormData("image", image.name, requestBody)
                    viewModel.kakaoUserInfo(id,nickname,body)
                }else {
                    viewModel.kakaoUserInfoWithOutProfile(id,nickname)
                }
            }else {
                Snackbar.make(add_user_info_activity,"????????? ?????? ????????????.",Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    private fun liveData(){
        viewModel.checkNicknameLiveData.observe(this, Observer {code ->
            if(code.equals("200")){
                kakao_nickname_input_layout.helperText = "????????? ?????? ??????"
                checkNick = true
            }else if(code.equals("204")){
                kakao_nickname_input_layout.error = "?????? ???????????? ??????????????????."
                checkNick = false
            }else{
                Snackbar.make(add_user_info_activity,"??????",Snackbar.LENGTH_SHORT).show()
                checkNick = false
            }
        })
        viewModel.kakaoUserInfoLD.observe(this, Observer { code ->
            if(code.equals("200")){
                Snackbar.make(add_user_info_activity,"????????? ?????? ??????",Snackbar.LENGTH_LONG).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
                finish()
            }else {
                Snackbar.make(add_user_info_activity,"???????????? ??????",Snackbar.LENGTH_SHORT).show()
                kakao_nick_et.text?.clear()
                kakao_nick_et.requestFocus()
            }
        })
    }
    //???????????? ???????????? ????????? intent??? ??????
    private fun openAlbum() {
        val imageIntent = Intent(Intent.ACTION_PICK)
        imageIntent.setDataAndType(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            "image/*"
        )
        getContent.launch(imageIntent)
    }

    //URI??? ?????? ????????? ??????
    private fun getRealPathFromURI(contentUri: Uri, context: Context): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(context, contentUri, proj, null, null, null)
        val cursor: Cursor = loader.loadInBackground()!!
        val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result: String = cursor.getString(column_index)
        cursor.close()

        return result
    }
    /*????????? ??????*/
    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                //TODO: ????????? ??? ?????? ????????? ??? ??????????????? ????????? ???????????? ??????
                openAlbum()
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                // ????????? ??? ?????? ??? ?????? ?????? ????????? ??????
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            }
            else -> {
                //????????? ????????? ????????? ????????? ????????? ???
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            }
        }
    }
}