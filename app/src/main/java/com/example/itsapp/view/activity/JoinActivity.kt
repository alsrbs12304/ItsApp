package com.example.itsapp.view.activity

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ProgressBar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.itsapp.R
import com.example.itsapp.viewmodel.JoinViewModel
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.example.itsapp.util.MailSender
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_join.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.mindrot.jbcrypt.BCrypt
import java.io.File
import java.util.regex.Pattern

class JoinActivity : AppCompatActivity() {

    private var userId: String = ""
    private var emailCode: String = ""
    private var checkId = false
    private var checkNick = false
    private var checkPw = false
    private var checkValidPw = false
    private var checkName = false
    private var checkEmail = false
    private var checkSend = false
    private var checkProfile = false

    private lateinit var selectedImageUri: Uri
    private val viewModel: JoinViewModel by viewModels()
    private val getContent: ActivityResultLauncher<Intent> = //
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null && it.data?.data != null) {
                selectedImageUri = it.data?.data!!
                Log.d("TAG", "onActivityResult: $selectedImageUri")
                Glide.with(applicationContext)
                    .load(selectedImageUri)
                    .circleCrop()
                    .into(profile_btn)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
        thread()
        liveData()
        btnEvent()
        textWarcher()
    }

    private fun btnEvent() {
        /*????????? ?????? ??????*/
        profile_btn.setOnClickListener {
            checkProfile = true
            checkPermission()
        }
        /*???????????? ??????*/
        join_btn.setOnClickListener {
            userId = join_id_edt.text.toString().trim()
            val password = join_password_edt.text.toString().trim()
            val encryptionPw = BCrypt.hashpw(password, BCrypt.gensalt())
            val userName = join_name_edt.text.toString().trim()
            val userNickName = join_nick_name_edt.text.toString().trim()
            val joinMethod = "??????"
            if (!checkName) {
                join_name_edt.requestFocus()
            } else if (!checkEmail) {
                Snackbar.make(join_activity, "????????? ???????????? ?????? ??????????????????.", Snackbar.LENGTH_SHORT).show()
            } else if (!checkNick && !checkId) {
                Snackbar.make(join_activity, "?????? ?????? ??????????????????.", Snackbar.LENGTH_SHORT).show()
            } else if (!checkPw) {
                Snackbar.make(join_activity, "???????????? ????????? ?????? ????????? ?????????.", Snackbar.LENGTH_SHORT).show()
            } else if (!checkValidPw) {
                Snackbar.make(join_activity, "??????????????? ?????? ?????? ????????????.", Snackbar.LENGTH_SHORT).show()
            } else {
                if(checkProfile){
                    val image = File(getRealPathFromURI(selectedImageUri, applicationContext))
                    val requestBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), image)

                    val body: MultipartBody.Part =
                        MultipartBody.Part.createFormData("image", image.name, requestBody)
                    viewModel.join(userId, encryptionPw, userName, userNickName, joinMethod,body)
                }else {
                    viewModel.joinWithoutProfile(userId, encryptionPw, userName, userNickName, joinMethod)
                }
                Snackbar.make(join_activity, "???????????? ??????.", Snackbar.LENGTH_SHORT).show()
            }
        }
        /*???????????? ??????*/
        back_btn.setOnClickListener {
            finish()
        }
        /*????????? ?????? ??????*/
        check_id.setOnClickListener {
            userId = join_id_edt.text.toString().trim()
            /*????????? ????????? ????????? ??????*/
            val pattern = Patterns.EMAIL_ADDRESS
            if (!userId.equals("") && pattern.matcher(userId).matches()) {
                viewModel.checkId(userId)
                loading_bar.visibility = ProgressBar.VISIBLE
            } else {
                id_input_layout.error = "????????? ?????????(?????????)??? ????????? ?????????."
                checkId = false
            }
        }
        /*????????? ?????? ??????*/
        check_nick.setOnClickListener {
            val userNickName = join_nick_name_edt.text.toString().trim()
            if (!userNickName.equals("")) {
                viewModel.checkNick(userNickName)
            } else {
                nickname_input_layout.error = "???????????? ??????????????????."
            }
        }
        /*???????????? ?????? ??????*/
        email_check_btn.setOnClickListener {
            val code = email_code_et.text?.trim().toString()
            if (code.equals(emailCode) && !code.equals("") && !checkEmail) {
                time_text.visibility = View.GONE
                checkEmail = true
                viewModel.countDownTimer.onFinish()
                Snackbar.make(join_activity, "????????? ?????? ?????????????????????.", Snackbar.LENGTH_SHORT).show()
            } else if (checkEmail) {
                Snackbar.make(join_activity, "?????? ?????? ??????????????????.", Snackbar.LENGTH_SHORT).show()
            } else if (!checkId) {
                Snackbar.make(join_activity, "???????????? ?????? ?????? ???????????????.", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(join_activity, "??????????????? ???????????????.", Snackbar.LENGTH_SHORT).show()
                time_text.text = ""
            }
        }
    }

    private fun textWarcher() {
        join_id_edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (join_id_edt.text!!.isEmpty()) {
                    id_input_layout.error = "?????????(?????????)??? ??????????????????."
                } else {
                    id_input_layout.error = null
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        join_password_edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val rule = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\$@\$!%*#?&]).{7,19}.\$"
                val password = join_password_edt.text.toString().trim()
                if (join_password_edt.text!!.isEmpty()) {
                    pw_input_layout.error = "??????????????? ???????????????."
                    checkPw = false
                } else if (Pattern.matches(rule, password)) {
                    pw_input_layout.error = null
                    checkPw = true
                } else {
                    pw_input_layout.error = "??????/??????/????????????(~!@#\$)??? 8~20?????? ???????????????."
                    checkPw = false
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        join_password_check_edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val password = join_password_edt.text.toString().trim()
                if (join_password_check_edt.text!!.isEmpty()) {
                    valid_id_layout.error = "??????????????? ???????????????."
                    checkValidPw = false
                } else if (join_password_check_edt.text.toString().trim().equals(password)) {
                    valid_id_layout.error = null
                    checkValidPw = true
                } else {
                    valid_id_layout.error = "??????????????? ???????????? ????????????."
                    checkValidPw = false
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        join_name_edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (join_name_edt.text!!.isEmpty()) {
                    name_input_layout.error = "????????? ??????????????????."
                    checkName = false
                } else {
                    name_input_layout.error = null
                    checkName = true
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        join_nick_name_edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (join_nick_name_edt.text!!.isEmpty()) {
                    nickname_input_layout.error = "???????????? ??????????????????."
                    checkNick = false
                } else {
                    nickname_input_layout.error = null
                    checkNick = false
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    /*LiveData*/
    private fun liveData() {
        /*????????? ?????? ?????? LIVEDATA*/
        viewModel.checkIdLiveData.observe(this, Observer { code ->
            if (code.equals("200")) {
                id_input_layout.helperText = "?????????(?????????) ????????????"
                checkId = true
                if (!checkSend) {
                    val sender = MailSender()
                    emailCode = sender.getEmailCode()
                    sender.sendEmail(
                        "ItsApp ????????????",
                        "ItsApp ???????????? ????????? ?????? ????????? ${emailCode}?????????.",
                        userId
                    )
                    loading_bar.visibility = ProgressBar.GONE
                    Snackbar.make(join_activity, "????????? ??????????????????.", Snackbar.LENGTH_SHORT).show()
                    viewModel.countDown()
                    checkSend = true
                } else if (checkSend) {
                    Snackbar.make(join_activity, "???????????? ?????? ??????????????????.", Snackbar.LENGTH_SHORT).show()
                }
            } else if (code.equals("204")) {
                loading_bar.visibility = ProgressBar.GONE
                id_input_layout.error = "?????? ???????????? ??????????????????."
                checkId = false
            } else {
                Snackbar.make(join_activity, "??????", Snackbar.LENGTH_SHORT).show()
                checkId = false
            }
        })
        /*???????????? ??????????????? LIVEDATA*/
        viewModel.count.observe(this, Observer {
            if (!it.equals("")) {
                time_text.text = it
            } else if (it.equals("")) {
                emailCode = ""
                time_text.visibility = View.GONE
                checkSend = false
            } else {
                emailCode = ""
            }
        })
        /*????????? ?????? ?????? LIVEDATA*/
        viewModel.checkNicknameLiveData.observe(this, Observer { code ->
            checkNick = if (code.equals("200")) {
                nickname_input_layout.helperText = "????????? ?????? ??????"
                true
            } else if (code.equals("204")) {
                nickname_input_layout.error = "?????? ???????????? ??????????????????."
                false
            } else {
                nickname_input_layout.error = "??????. ??????????????? ?????? ????????????."
                false
            }
        })
        /*???????????? LIVEDATA*/
        viewModel.joinLiveData.observe(this, Observer { code ->
            if (code.equals("200")) {
                Snackbar.make(join_activity, "???????????? ??????", Snackbar.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
                finish()
            } else {
                Snackbar.make(join_activity, "???????????? ??????", Snackbar.LENGTH_SHORT).show()
                join_id_edt.text?.clear()
                join_id_edt.requestFocus()
                join_password_edt.text?.clear()
                join_password_check_edt.text?.clear()
                join_name_edt.text?.clear()
                join_nick_name_edt.text?.clear()
                email_code_et.text?.clear()
            }
        })
    }

    /*??????????????? ?????????*/
    private fun thread() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build()
        );
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