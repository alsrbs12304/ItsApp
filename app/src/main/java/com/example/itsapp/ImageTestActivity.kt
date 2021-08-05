package com.example.itsapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.itsapp.viewmodel.DeviceViewModel
import kotlinx.android.synthetic.main.activity_image_test.*
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class ImageTestActivity : AppCompatActivity() {

    private val isRunning = true
    private val viewModel : DeviceViewModel by viewModels()

    private lateinit var bitmap : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_test)

        val deviceName = "Mac Book Pro 16형 Intel Core i7 1TB 용량"
        val thread=ThreadClass()
        thread.start()
    }
    inner class ThreadClass : Thread(){
        override fun run() {
            while(isRunning){
                try {
                    val urlString = "http://ec2-54-180-29-97.ap-northeast-2.compute.amazonaws.com:3000/home/ubuntu/app/nodejs/ItsApp/images/MacBook.jpeg"
                    val url = URL(urlString)
                    val conn = url.openConnection() as HttpURLConnection

                    conn.doInput
                    conn.connect()

                    val inputStream : InputStream
                    inputStream = conn.inputStream
                    bitmap = BitmapFactory.decodeStream(inputStream)

                    runOnUiThread {
                        image_view.setImageBitmap(bitmap)
                    }
                }catch (e:MalformedURLException){
                    e.printStackTrace()
                }catch (e:IOException){
                    e.printStackTrace()
                }
            }
        }
    }
}