package com.example.itsapp.view.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.example.itsapp.R
import com.example.itsapp.view.activity.FavoritesActivity
import com.example.itsapp.view.activity.SplashActivity
import com.example.itsapp.view.activity.UpdateUserInfoActivity
import com.example.itsapp.viewmodel.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_my_page.*
import kotlinx.android.synthetic.main.fragment_my_page.profile_btn
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class MyPageFragment : Fragment() {

    private val viewModel:HomeViewModel by viewModels()
    private var userNickname:String=""
    private var userId = ""
    private var loginMethod:String=""
    private var profileUrl:String=""
    private lateinit var selectedImageUri: Uri
    private val getContent: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null && it.data?.data != null) {
                selectedImageUri = it.data?.data!!
                uploadImage(selectedImageUri,requireContext(),userId)
                viewModel.userInfo(loginMethod)
            }
        }
    companion object{
        const val TAG : String = "로그"

        fun newInstance() : MyPageFragment{
            return MyPageFragment()
        }
    }

    // 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MyPageFragment -onCreate() called")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "MyPageFragment -onAttach() called")
    }

    // 뷰가 생성됐을 때
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my_page,container,false)
        return view
    }

    override fun onStart() {
        super.onStart()
        loginMethod = viewModel.getLoginMethod()!!
        mypage_login_method.text = loginMethod
    }

    override fun onResume() {
        super.onResume()
        viewModel.userInfo(loginMethod)
        liveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*myPage에서 이미지 수정 버튼*/
        profile_btn.setOnClickListener{
            checkPermission()
        }
        /*내정보 수정*/
        update_user_info.setOnClickListener {
            startActivity(Intent(activity,UpdateUserInfoActivity::class.java))
            activity?.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
        /*내 리뷰*/
        my_review.setOnClickListener {
            //TODO: 내 리뷰 페이지
        }
        /*탈퇴하기 버튼*/
        mypage_retire.setOnClickListener{
            val builder = AlertDialog.Builder(activity)
            builder.setMessage("정말 탈퇴하시겠습니까?")
                .setPositiveButton("탈퇴하기"){dialogInterface, i ->
                    viewModel.retireApp(loginMethod)
                }
                .setNegativeButton("취소"){dialogInterface, i ->

                }
                .show()
        }
        /*문의하기 버튼*/
        inquire_btn.setOnClickListener{
            val url = TalkApiClient.instance.channelChatUrl("_lELGs")
            KakaoCustomTabsClient.openWithDefault(requireContext(),url)
        }
        /*로그아웃 버튼*/
        mypage_logout.setOnClickListener{
            disconnect()
            startActivity(Intent(activity,SplashActivity::class.java))
            activity?.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }

        // 즐겨찾기 (민균)
        go_to_favorites.setOnClickListener {
            startActivity(Intent(activity,FavoritesActivity::class.java))
        }
    }
    /*라이브데이터*/
    fun liveData(){
        viewModel.userInfoLiveData.observe(this, Observer {
            userId = it.jsonArray.userId
            userNickname = it.jsonArray.userNickname
            profileUrl =it.jsonArray.profileUrl
            Log.d(TAG, "liveData: $userNickname")
            mypage_nickname.text = userNickname
            Glide.with(requireContext())
                .load(profileUrl)
                .fallback(R.drawable.profile_img)
                .circleCrop()
                .into(profile_btn)
        })
        viewModel.retireLiveData.observe(this, Observer {
            if(it=="200"){
                disconnect()
                val intent = Intent(activity,SplashActivity::class.java)
                startActivity(intent)
                activity?.overridePendingTransition(R.anim.right_in, R.anim.left_out)
                activity?.finish()
            }else {
                Snackbar.make(home_activity, "회원 탈퇴 오류", Snackbar.LENGTH_SHORT).show()
            }
        })
    }
    /*로그아웃,회원탈퇴시 sharedpreference 쿠키 삭제와 카카오 자동로그인 해제 함수*/
    fun disconnect(){
        viewModel.logoutPref()
        if(loginMethod == "카카오"){
            UserApiClient.instance.logout {error ->
                if(error !=null){
                    Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                }
                else {
                    Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                }
            }
        }
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
    private fun uploadImage(imageUri: Uri, context: Context,userId:String) {
        val image: File = File(getRealPathFromURI(imageUri, context))
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), image)

        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", image.name, requestBody)

        viewModel.updateImage(body,userId)
    }
    /*퍼미션 체크*/
    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
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