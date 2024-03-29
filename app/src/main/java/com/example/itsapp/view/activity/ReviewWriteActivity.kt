package com.example.itsapp.view.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.example.itsapp.R
import com.example.itsapp.viewmodel.DeviceViewModel
import com.example.itsapp.viewmodel.HomeViewModel
import com.example.itsapp.viewmodel.ReviewViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_device_info.*
import kotlinx.android.synthetic.main.activity_review_write.*
import kotlinx.android.synthetic.main.activity_review_write.back_btn
import kotlinx.android.synthetic.main.activity_review_write.device_brand
import kotlinx.android.synthetic.main.activity_review_write.device_img
import kotlinx.android.synthetic.main.activity_review_write.device_name
import kotlinx.android.synthetic.main.activity_review_write.rating_bar
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ReviewWriteActivity : AppCompatActivity() {
    private val deviceViewModel: DeviceViewModel by viewModels()
    private val reviewViewModel: ReviewViewModel by viewModels()
    private val homeViewModel : HomeViewModel by viewModels()
    private val SELECT_IMAGE = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_write)

        back_btn.setOnClickListener {
            finish()
        }

        rating_bar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            review_point.text = ratingBar.rating.toString()
        }

        val intent = intent
        val deviceName = intent.getStringExtra("deviceName")
        Log.i("getString", deviceName.toString())

        deviceViewModel.getDeviceInfo(deviceName!!)

        deviceViewModel.choiceDeviceImg(deviceName)
        liveData()

        deviceViewModel.deviceInfoLiveData.observe(this, Observer { deviceInfo ->
            if(deviceInfo.code.equals("200")){
                device_brand.text = deviceInfo.jsonArray[0].deviceBrand
                device_name.text = deviceInfo.jsonArray[0].deviceName
            }
        })

        content_pros_edt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                content_pros_edt_length.text = "0"
                if(content_pros_edt.length() < 20)
                    content_pros_edt_length.setTextColor(ContextCompat.getColor(applicationContext,R.color.red700))
                else
                    content_pros_edt_length.setTextColor(ContextCompat.getColor(applicationContext,R.color.blue400))
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var input = content_pros_edt.text.toString()
                content_pros_edt_length.text = input.length.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                var input = content_pros_edt.text.toString()
                content_pros_edt_length.text = input.length.toString()
            }
        })
        content_cons_edt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                content_cons_edt_length.text = "0"

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var input = content_cons_edt.text.toString()
                content_cons_edt_length.text = input.length.toString()
                if(content_cons_edt.length() < 20)
                    content_cons_edt_length.setTextColor(ContextCompat.getColor(applicationContext,R.color.red700))
                else
                    content_cons_edt_length.setTextColor(ContextCompat.getColor(applicationContext,R.color.blue400))
            }

            override fun afterTextChanged(s: Editable?) {
                var input = content_cons_edt.text.toString()
                content_cons_edt_length.text = input.length.toString()
            }

        })

        write_btn.setOnClickListener {
            val deviceName = deviceName
            // 로그인된 회원의 아이디를 받아와야댐
            val userId = homeViewModel.getLoginSession()
            val reviewPoint:Int = rating_bar.rating.toInt()
            val contentPros = content_pros_edt.text.toString()
            val contentCons = content_cons_edt.text.toString()
            if(reviewPoint == 0){
                Snackbar.make(review_write_layout,"리뷰 점수를 선택해주세요.",Snackbar.LENGTH_SHORT).show()
            }
            else if(contentPros.length < 20){
                Snackbar.make(review_write_layout,"좋았던 점에 대한 리뷰 내용을 20자 이상 적어주세요.",Snackbar.LENGTH_SHORT).show()
            }else if(contentCons.length < 20){
                Snackbar.make(review_write_layout,"아쉬운 점에 대한 리뷰 내용을 20자 이상 적어주세요.",Snackbar.LENGTH_SHORT).show()
            }else{
                reviewViewModel.writeReview(deviceName,userId,reviewPoint,contentPros,contentCons)
            }
        }
        reviewViewModel.writeReviewLiveData.observe(this, Observer {
            if(it.equals("200")){
                finish()
                Toast.makeText(this,"리뷰 등록 완료!",Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DeviceInfoActivity::class.java)
                intent.putExtra("deviceName",deviceName)
                startActivity(intent)
            }else if(it.equals("204")){
                Toast.makeText(this,"해당 제품에 대해 이미 리뷰를 작성했씁니다.",Toast.LENGTH_SHORT).show()
            }
        })


//        img_upload_btn.setOnClickListener {
//            checkPermission()
//        }
    }
    fun liveData(){
        deviceViewModel.choiceDeviceImgLiveData.observe(this, Observer {
            if(it.code == "200"){
                Glide.with(this)
                    .load(it.jsonArray[0].imgUrl)
                    .into(device_img)
            }else{
                Snackbar.make(home_fragment,"이미지 로드 오류",Snackbar.LENGTH_SHORT).show()
            }
        })
    }


    //------------------------------
//    private fun checkPermission(){
//        when{
//            ContextCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.READ_EXTERNAL_STORAGE
//            )== PackageManager.PERMISSION_GRANTED -> {
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
//    fun showContextPopupPermission(){
//        AlertDialog.Builder(this).setTitle("권한이 필요합니다")
//            .setMessage("사진을 불러오기 위해 권한이 필요합니다")
//            .setPositiveButton("동의하기") { _, _ ->
//                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
//            }
//            .setNegativeButton("취소하기") { _, _ ->}
//            .create()
//            .show()
//
//    }
//    private fun uploadImage(imageUri:Uri, context: Context) {
//        val image: File = File(getRealPathFromURI(imageUri,context))
//        val requestBody: RequestBody = RequestBody.create(MediaType.parse("image/*"),image)
//
//        val body: MultipartBody.Part = MultipartBody.Part.createFormData("image",image.name,requestBody)
//
//        reviewViewModel.uploadImage(body)
//    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        val selectedImageUri: Uri
//        if(requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null && data.data != null){
//            selectedImageUri = data.data!!
//            uploadImage(selectedImageUri,applicationContext)
//            Log.d("TAG", "onActivityResult: "+selectedImageUri)
//            Glide.with(applicationContext)
//                .load(selectedImageUri)
//                .circleCrop()
//                .into(review_img_check)
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