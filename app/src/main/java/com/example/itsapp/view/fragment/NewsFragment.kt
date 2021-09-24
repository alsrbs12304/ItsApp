package com.example.itsapp.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itsapp.R
import com.example.itsapp.view.activity.NewsActivity
import com.example.itsapp.view.adapter.BlogAdapter
import com.example.itsapp.view.adapter.NewsAdapter
import com.example.itsapp.viewmodel.NewsViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_blog.*
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.fragment_news.tips_tab

class NewsFragment : Fragment() {

    private val viewModel: NewsViewModel by viewModels()
    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var blogRecyclerView: RecyclerView
    private var userAge:String =""
    private var userSex:String =""
    private var userJob:String = ""
    companion object{
        fun newInstance() : NewsFragment {
            return NewsFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView(view)
        LiveData(view)
        btnEvent(view)
        viewModel.surveyParticipation()
    }

    fun LiveData(view:View){
        viewModel.newsLiveData.observe(viewLifecycleOwner, Observer {
            var result = it.items
            val mAdapter = this!!.activity?.let { it1 -> NewsAdapter(result, it1) }
            newsRecyclerView.adapter = mAdapter
        })
        viewModel.blogLiveData.observe(viewLifecycleOwner, Observer {
            var result = it.items
            val mAdapter = this!!.activity?.let { it1 -> BlogAdapter(result, it1) }
            blogRecyclerView.adapter = mAdapter
        })
        viewModel.participationLiveData.observe(viewLifecycleOwner, Observer {
            if(it.code == "204"){
                if(it.jsonArray.participation=="y"){
                    userAge = it.jsonArray.userAge
                    userJob = it.jsonArray.userJob
                    userSex = it.jsonArray.userSex
                }
            }else {

            }
        })
    }
    fun recyclerView(view:View){
        newsRecyclerView = view.findViewById(R.id.news_rv)
        var NewsLayoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL,false)
        newsRecyclerView.layoutManager = NewsLayoutManager
        viewModel.searchReadNews("맥북",1,100)
    }
    fun btnEvent(view: View){
        tips_tab.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabSelected(p0: TabLayout.Tab?) {
                val position = p0?.position
                when(position){
                    0 -> {
                        viewModel.searchReadNews("맥북",1,100)
                    }
                    1 -> {
                        viewModel.searchReadNews("삼성 노트북",1,100)
                    }
                    2 -> {
                        viewModel.searchReadNews("LG 노트북",1,100)
                    }
                    3 -> {
                        viewModel.searchReadNews("ASUS 노트북",1,100)
                    }
                    4 -> {
                        viewModel.searchReadNews("DELL 노트북",1,100)
                    }
                    5 -> {
                        viewModel.searchReadNews("LENOVO 노트북",1,100)
                    }
                }
            }
        })
    }
}