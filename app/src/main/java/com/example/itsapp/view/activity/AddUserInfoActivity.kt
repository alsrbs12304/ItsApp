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
                val image = File(getRealPathFromURI(selectedImageUri, applicationContext))
                val requestBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), image)

                val body: MultipartBody.Part =
                    MultipartBody.Part.createFormData("image", image.name, requestBody)
                viewModel.kakaoUserInfo(id,nickname,body)
            }else {
                Snackbar.make(add_user_info_activity,"닉네임 체크 해주세요.",Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    private fun liveData(){
        viewModel.checkNicknameLiveData.observe(this, Observer {code ->
            if(code.equals("200")){
                kakao_nickname_input_layout.helperText = "닉네임 사용 가능"
                checkNick = true
            }else if(code.equals("204")){
                kakao_nickname_input_layout.error = "이미 사용중인 닉네임입니다."
                checkNick = false
            }else{
                Snackbar.make(add_user_info_activity,"에러",Snackbar.LENGTH_SHORT).show()
                checkNick = false
            }
        })
        viewModel.kakaoUserInfoLD.observe(this, Observer { code ->
            if(code.equals("200")){
                Snackbar.make(add_user_info_activity,"닉네임 설정 성공",Snackbar.LENGTH_LONG).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
                finish()
            }else {
                Snackbar.make(add_user_info_activity,"회원가입 실패",Snackbar.LENGTH_SHORT).show()
                kakao_nick_et.text?.clear()
                kakao_nick_et.requestFocus()
            }
        })
    }
    //앨범에서 이미지를 가져와 intent로 전송
    private fun openAlbum() {
        val imageIntent = Intent(Intent.ACTION_PICK)
        imageIntent.setDataAndType(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            "image/*"
        )
        getContent.launch(imageIntent)
    }

    //URI를 실제 경로로 변환
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
    /*퍼미션 체크*/
    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                //TODO: 권한이 잘 부여 되었을 때 갤러리에서 사진을 선택하는 기능
                openAlbum()
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                // 교육용 팝 확인 후 권한 팝업 띄우는 기능
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            }
            else -> {
                //초기에 아무런 퍼미션 요청이 없었을 때
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            }
        }
    }
}