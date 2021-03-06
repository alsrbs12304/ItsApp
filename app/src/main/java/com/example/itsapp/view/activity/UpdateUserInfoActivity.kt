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
                update_nickname_input_layout.helperText = "????????? ?????? ??????"
                true
            } else if (it.equals("204")) {
                update_nickname_input_layout.error = "?????? ???????????? ??????????????????."
                false
            } else {
                update_nickname_input_layout.error = "??????. ??????????????? ?????? ????????????."
                false
            }
        })
    }

    fun btnClick(){
        /*?????? ???????????? ??????*/
        back_btn.setOnClickListener {
            finish()
        }
        /*????????????*/
        nick_check_btn.setOnClickListener {
            editedNickname = update_nick_et.text.toString().trim()
            if(editedNickname != "")
            {
                if(currentNickname != editedNickname)
                    viewModel.checkNick(editedNickname)
                else
                    update_nickname_input_layout.error = "???????????? ???????????????!"
            }
            else if(editedNickname == "")
                update_nickname_input_layout.error = "???????????? ??????????????????."
        }
        /*????????? ?????? ??????*/
        profile_btn.setOnClickListener {
            checkProfile = true
            checkPermission()
        }
        /*???????????? ??????*/
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
                Snackbar.make(update_user_info_activity,"????????? ????????? ????????????.",Snackbar.LENGTH_SHORT).show()
        }
    }
    private fun glideImage(uri: Uri){
        Glide.with(this)
            .load(uri)
            .fallback(R.drawable.profile_img)
            .circleCrop()
            .into(profile_btn)
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
    private fun uploadImage(imageUri: Uri, context: Context,userId:String, nickname :String) {
        val image: File = File(getRealPathFromURI(imageUri, context))
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), image)

        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", image.name, requestBody)

        viewModel.updateUserProfile(userId,nickname,body)
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