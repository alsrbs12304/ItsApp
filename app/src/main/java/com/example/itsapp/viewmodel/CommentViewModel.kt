package com.example.itsapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.itsapp.model.vo.comment.CommentInfo
import com.example.itsapp.model.vo.user.UserInfo
import com.example.itsapp.retrofit.APIInterface
import com.example.itsapp.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class CommentViewModel(application: Application): AndroidViewModel(application) {
    val context = getApplication<Application>().applicationContext
    val service: APIInterface = RetrofitClient.getInstance(context).create(APIInterface::class.java)

    val commentLiveData = MutableLiveData<CommentInfo>()
    val writeCommentLiveData = MutableLiveData<CommentInfo>()
    val deleteCommentLiveData = MutableLiveData<String>()
    val userNickNameLiveData = MutableLiveData<UserInfo>()

    fun getComment(deviceName : String, reviewWriter : String){
        viewModelScope.launch {
            val data: CommentInfo = service.getComment(deviceName, reviewWriter)
            commentLiveData.value = data
        }
    }

    fun writeComment(deviceName: String, reviewWriter: String, writer:String, commentContent:String,writeTime : String){
        viewModelScope.launch {
            val data: CommentInfo = service.writeComment(deviceName, reviewWriter, writer, commentContent,writeTime)
            writeCommentLiveData.value = data
        }
    }
    fun deleteComment(commentId : Int, writer: String){
        viewModelScope.launch {
            val data:String = service.deleteComment(commentId,writer)
            deleteCommentLiveData.value = data
        }
    }
    fun getUserNickName(userId:String){
        viewModelScope.launch {
            val data : UserInfo = service.getUserNickName(userId)
            userNickNameLiveData.value = data
        }
    }
}