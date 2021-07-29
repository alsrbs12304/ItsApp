package com.example.itsapp.retrofit

import com.example.itsapp.model.vo.*
import com.example.itsapp.model.vo.DeviceInfo
import com.example.itsapp.model.vo.UserInfo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface APIInterface {

    /*회원가입*/
    @FormUrlEncoded
    @POST("/android/join")
    suspend fun join(
        @Field("userId") userId: String,
        @Field("userPw") userPw: String,
        @Field("userName") userName: String,
        @Field("userNickName") userNickname : String,
        @Field("loginMethod") loginMethod: String
    ):String

    /*아이디 중복 검사*/
    @GET("/android/checkId")
    suspend fun checkId(@Query("userId") userId:String):String

    /*닉네임 중복 검사*/
    @GET("/android/checkNick")
    suspend fun checkNick(@Query("userNickName") userNickName: String):String

    /*로그인*/
    @GET("/android/login")
    suspend fun login(@Query("userId") userId: String):UserInfo

    /*Kakao 로그인*/
    @FormUrlEncoded
    @POST("/android/kakaoLogin")
    suspend fun kakaoLogin(
        @Field("userId") userId:String,
        @Field("userName") userName:String
    ):String

    /*유저 정보 검사*/
    @GET("/android/seceondJoin")
    suspend fun seceondJoin(
        @Query("userId") userId:String
    ):String

    /*카카오 닉네임 설정*/
    @FormUrlEncoded
    @POST("/android/kakaoUserInfo")
    suspend fun kakaoUserInfo(
        @Field("userId") userId:String,
        @Field("userNickname") userNickname: String
    ):String

    /*비밀번호 변경*/
    @FormUrlEncoded
    @POST("/android/updatePw")
    suspend fun updatePw(@Field("userId") userID:String,@Field("userPw") userPw:String):String

    @GET("/android/getDevice")
    suspend fun getDevice(
        @Query("deviceBrand") deviceBrand : String
    ) : DeviceInfo

    @GET("/android/getReviewCnt")
    suspend fun getReviewCnt() : DeviceInfo

    @GET("/android/getDeviceInfo")
    suspend fun getDeviceInfo(
        @Query("deviceName") deviceName : String
    ) : DeviceInfo

    @GET("/android/getReviewAll")
    suspend fun getReviewAll(
        @Query("deviceName") deviceName: String
    ) : ReviewInfo

    // 상위 3개 리뷰 갖고오는 함수
    @GET("/android/getReviewThird")
    suspend fun getReviewThird(
        @Query("deviceName") deviceName : String
    ) : ReviewInfo

    // 해당 디바이스 리뷰 점수 별 리뷰 개수 갖고오는 함수
    @GET("/android/getReviewPointCount")
    suspend fun getReviewPointCount(
        @Query("deviceName") deviceName : String
    ) : DeviceInfo

    @FormUrlEncoded
    @POST("/android/writeReview")
    suspend fun writeReview(
        @Field("deviceName") deviceName: String,
        @Field("userId") userId: String,
        @Field("reviewPoint") reviewPoint: Int,
        @Field("contentPros") contentPros: String,
        @Field("contentCons") contentCons: String
    ) : ReviewInfo

    /*유저 성별, 나이, 직업 불러오는 함수*/
    @FormUrlEncoded
    @POST("/android/userServey")
    suspend fun userJob(
        @Field("userSex") userSex:String,
        @Field("userAge") userAge:String,
        @Field("userJob") userJob:String
    ) :String

    /*유저 정보 설문조사 참여 여부*/
    @GET("/android/surveyParticipation")
    suspend fun surveyParticipation() :userDetailInfo

    /*유저 정보*/
    @GET("/android/userInfo")
    suspend fun userInfo(
        @Query("loginMethod") loginMethod: String
    ):UserInfo

    @GET("/android/getChoiceReview")
    suspend fun getChoiceReview(
        @Query("deviceName") deviceName : String,
        @Query("writer") writer : String
    ) : ReviewInfo

    @GET("/android/getComment")
    suspend fun getComment(
        @Query("deviceName") deviceName : String,
        @Query("reviewWriter") reviewWriter : String
    ) : CommentInfo

    @FormUrlEncoded
    @POST("/android/writeComment")
    suspend fun writeComment(
        @Field("deviceName") deviceName : String,
        @Field("reviewWriter") reviewWriter: String,
        @Field("writer") writer: String,
        @Field("commentContent") commentContent : String
    ) : CommentInfo

    @GET("/android/getLoginUserId")
    suspend fun getLoginUserId(
        @Query("userId") userId: String
    ) : UserInfo

    @GET("/android/deleteReview")
    suspend fun deleteReview(
        @Query("deviceName") deviceName: String,
        @Query("writer") writer: String
    ) : ReviewInfo

    @GET("/android/deleteComment")
    suspend fun deleteComment(
        @Query("commentId") commentId : Int
    ) : CommentInfo

    @FormUrlEncoded
    @POST("/android/retireApp")
    suspend fun retireApp(
        @Field("loginMethod") loginMethod: String
    ):String

    @GET("/android/getRank")
    suspend fun getRank()

    /*이미지 업로드*/
    @FormUrlEncoded
    @POST("/upload")
    suspend fun upload(@Field("image") image:String) : String
}