package com.example.itsapp.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.itsapp.*
import com.example.itsapp.viewmodel.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private var deviceFragment = DeviceFragment()
    private val viewModel:HomeViewModel by viewModels()

    companion object{
        const val TAG : String = "로그"
        fun newInstance() : HomeFragment{
            return HomeFragment()
        }
    }

    // 뷰가 생성됐을 때
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home,container,false)

        cardviewTranscation(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.brandImg()
        liveData()
    }
    fun liveData(){
        viewModel.brandImgLiveData.observe(viewLifecycleOwner, Observer {
            if(it.code == "200"){
                Glide.with(this)
                    .load(it.brand[0].imgUrl)
                    .into(apple_logo)
                Glide.with(this)
                    .load(it.brand[1].imgUrl)
                    .into(asus_logo)
                Glide.with(this)
                    .load(it.brand[2].imgUrl)
                    .into(dell_logo)
                Glide.with(this)
                    .load(it.brand[3].imgUrl)
                    .into(lenovo_logo)
                Glide.with(this)
                    .load(it.brand[4].imgUrl)
                    .into(lg_logo)
                Glide.with(this)
                    .load(it.brand[5].imgUrl)
                    .into(samsung_logo)
            }else{
                Snackbar.make(home_fragment,"이미지 로드 오류",Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun cardviewTranscation(view:View) {
        val apple by lazy { view.findViewById<CardView>(R.id.apple_cardview) }
        val asus by lazy { view.findViewById<CardView>(R.id.asus_cardview) }
        val samsung by lazy { view.findViewById<CardView>(R.id.samsung_cardview) }
        val lenovo by lazy { view.findViewById<CardView>(R.id.lenovo_cardview) }
        val lg by lazy { view.findViewById<CardView>(R.id.lg_cardview) }
        val dell by lazy { view.findViewById<CardView>(R.id.dell_cardview) }

        // 브랜드별 프래그먼트들을 만들지 말고 하나만 만들어서
        // 클릭된게 apple_cardview 이면 브랜드 프래그먼트에 Apple 이라는 걸 bundle 객체를 생성해(key와 value) 저장한다음
        // 브랜드 프래그먼트로 전달한다.
        apple.setOnClickListener{
            var bundle = Bundle()
            bundle.putString("deviceBrand","Apple")
            deviceFragment.arguments = bundle
            Log.d("putString", bundle.toString())
            activity?.supportFragmentManager!!.beginTransaction().replace(R.id.container,deviceFragment).commit()
        }
        lg.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("deviceBrand","LG")
            deviceFragment.arguments = bundle
            activity?.supportFragmentManager!!.beginTransaction().replace(R.id.container,deviceFragment).commit()
        }
        samsung.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("deviceBrand","SAMSUNG")
            deviceFragment.arguments = bundle
            activity?.supportFragmentManager!!.beginTransaction().replace(R.id.container,deviceFragment).commit()
        }
        dell.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("deviceBrand","DELL")
            deviceFragment.arguments = bundle
            activity?.supportFragmentManager!!.beginTransaction().replace(R.id.container,deviceFragment).commit()
        }
        lenovo.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("deviceBrand","LENOVO")
            deviceFragment.arguments = bundle
            activity?.supportFragmentManager!!.beginTransaction().replace(R.id.container,deviceFragment).commit()
        }
        asus.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("deviceBrand","ASUS")
            deviceFragment.arguments = bundle
            activity?.supportFragmentManager!!.beginTransaction().replace(R.id.container,deviceFragment).commit()
        }
    }
}