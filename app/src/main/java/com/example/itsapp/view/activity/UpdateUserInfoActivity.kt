package com.example.itsapp.view.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.SpannableStringBuilder
import android.util.Log
import android.widget.ProgressBar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.example.itsapp.R
import com.example.itsapp.view.fragment.MyPageFragment
import com.example.itsapp.viewmodel.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_join.*
import kotlinx.android.synthetic.main.activity_update_user_info.*
import kotlinx.android.synthetic.main.activity_update_user_info.back_btn
import kotlinx.android.synthetic.main.activity_update_user_info.profile_btn
import kotlinx.android.synthetic.main.fragment_my_page.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UpdateUserInfoActivity : AppCompatActivity() {
    private val viewModel:HomeViewModel by viewModels()
    private lateinit var loginMethod:String
    private lateinit var userId:String
    private lateinit var currentNickname:String
    private lateinit var editedNickname:String
    private lateinit var selectedImageUri: Uri
    private var checkNick = false
    private var checkProfile = false
    private val getContent: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null && it.data?.data != null) {
                selectedImageUri = it.data?.data!!
                glideImage(selectedImageUri)
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user_info)
        btnClick()
        liveData()
        loginMethod = viewModel.getLoginMethod()!!
        viewModel.userInfo(loginMethod)
    }

    fun liveData(){
        viewModel.userInfoLiveData.observe(this, Observer {
            userId = it.jsonArray.userId
            id_tv.text = userId
            currentNickname = it.jsonArray.userNickname
            if(it.jsonArray.profileUrl ==null){
                Glide.with(this)
                    .load(it.jsonArray.profileUrl)
                    .fallback(R.drawable.profile_img)
                    .circleCrop()
                    .into(profile_btn)
            }else{
                selectedImageUri = Uri.parse(it.jsonArray.profileUrl)
                glideImage(selectedImageUri)
            }
            update_nick_et.text = SpannableStringBuilder(currentNickname)
        })
        viewModel.checkNicknameLiveData.observe(this, Observer {
            checkNick = if (it.equals("200")) {
                update_nickname_input_layout.helperText = "닉네임 사용 가능"
                true
            } else if (it.equals("204")) {
                update_nickname_input_layout.error = "이미 사용중인 닉네임입니다."
                false
            } else {
                update_nickname_input_layout.error = "에러. 고객센터에 문의 바랍니다."
                false
            }
        })
    }

    fun btnClick(){
        /*툴바 뒤로가기 버튼*/
        back_btn.setOnClickListener {
            finish()
        }
        /*중복검사*/
        nick_check_btn.setOnClickListener {
            editedNickname = update_nick_et.text.toString().trim()
            if(editedNickname != "")
            {
                if(currentNickname != editedNickname)
                    viewModel.checkNick(editedNickname)
                else
                    update_nickname_input_layout.error = "닉네임이 그대로에요!"
            }
            else if(editedNickname == "")
                update_nickname_input_layout.error = "닉네임을 입력해주세요."
        }
        /*프로필 수정 버튼*/
        profile_btn.setOnClickListener {
            checkProfile = true
            checkPermission()
        }
        /*수정하기 버튼*/
        update_btn.setOnClickListener {
            if(checkNick&&checkProfile){
                update_loading_bar.visibility = ProgressBar.VISIBLE
                uploadImage(selectedImageUri,this,userId,editedNickname)
                Handler(Looper.getMainLooper()).postDelayed({
                    update_loading_bar.visibility = ProgressBar.GONE
                    finish()
                },1000)
            }else if(!checkNick&&checkProfile){
                update_loading_bar.visibility = ProgressBar.VISIBLE
                uploadImage(selectedImageUri,this,userId,currentNickname)
                Handler(Looper.getMainLooper()).postDelayed({
                    update_loading_bar.visibility = ProgressBar.GONE
                    finish()
                },1000)
            }
            else if(checkNick&&!checkProfile){
                update_loading_bar.visibility = ProgressBar.VISIBLE
                viewModel.updateUserInfo(userId,editedNickname)
                Handler(Looper.getMainLooper()).postDelayed({
                    update_loading_bar.visibility = ProgressBar.GONE
                    finish()
                },1000)
            }
            else if(!checkNick&&!checkProfile)
                Snackbar.make(update_user_info_activity,"변경된 사항이 없습니다.",Snackbar.LENGTH_SHORT).show()
        }
    }
    private fun glideImage(uri: Uri){
        Glide.with(this)
            .load(uri)
            .fallback(R.drawable.profile_img)
            .circleCrop()
            .into(profile_btn)
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
    private fun uploadImage(imageUri: Uri, context: Context,userId:String, nickname :String) {
        val image: File = File(getRealPathFromURI(imageUri, context))
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), image)

        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", image.name, requestBody)

        viewModel.updateUserProfile(userId,nickname,body)
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