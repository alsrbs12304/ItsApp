package com.example.itsapp.retrofit

import com.example.itsapp.model.vo.*
import com.example.itsapp.model.vo.device.DeviceInfo
import com.example.itsapp.model.vo.user.UserInfo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import com.example.itsapp.model.vo.comment.CommentInfo
import com.example.itsapp.model.vo.favorites.FavoritesInfo
import com.example.itsapp.model.vo.review.ReviewInfo
import com.example.itsapp.model.vo.spec.SpecInfo
import retrofit2.http.*

interface APIInterface {

    /*회원가입*/
    @Multipart
    @POST("/android/join")
    suspend fun join(
        @Query("userId") userId: String,
        @Query("userPw") userPw: String,
        @Query("userName") userName: String,
        @Query("userNickName") userNickname : String,
        @Query("loginMethod") loginMethod: String,
        @Part image:MultipartBody.Part
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
    @Multipart
    @POST("/android/kakaoUserInfo")
    suspend fun kakaoUserInfo(
        @Query("userId") userId:String,
        @Query("userNickname") userNickname: String,
        @Part image:MultipartBody.Part
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
    ) : String

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
    ) : String

    @GET("/android/deleteComment")
    suspend fun deleteComment(
        @Query("commentId") commentId : Int
    ) : String

    @FormUrlEncoded
    @POST("/android/retireApp")
    suspend fun retireApp(
        @Field("loginMethod") loginMethod: String
    ):String

    /*이미지 업로드*/
    @Multipart
    @POST("/android/updateImage")
    suspend fun updateImage(@Part image:MultipartBody.Part,@Query("userId") userId:String) : String

    @GET("/android/brandImg")
    suspend fun brandImg():BrandImage

    @GET("/android/getSpec")
    suspend fun getSpec(
        @Query("deviceName") deviceName: String
    ) : DeviceInfo

    // 즐겨찾기 담기
    @FormUrlEncoded
    @POST("/android/addFavorites")
    suspend fun addFavorites(
        @Field("deviceName") deviceName : String,
        @Field("userId") userId: String
    ) : FavoritesInfo

    @GET("/android/getFavorites")
    suspend fun getFavorites(
        @Query("userId") userId : String
    ) : DeviceInfo

    @GET("/android/deleteFavorites")
    suspend fun deleteFavorites(
        @Query("userId") userId: String,
        @Query("deviceName") deviceName: String
    ) : String

    @Multipart
    @POST("/android/updateUserProfile")
    suspend fun updateUserProfile(
        @Query("userId") userId:String,
        @Query("userNickName") userNickName:String,
        @Part image:MultipartBody.Part
    ):String

    @FormUrlEncoded
    @POST("/android/updateUserInfo")
    suspend fun updateUserInfo(
        @Field("userId") userId:String,
        @Field("userNickName") userNickName:String,
    ):String
}