package com.example.itsapp.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itsapp.R
import com.example.itsapp.view.adapter.NewsAdapter
import com.example.itsapp.viewmodel.NewsViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_blog.*
import kotlinx.android.synthetic.main.fragment_issue.*
import kotlinx.android.synthetic.main.fragment_issue.tips_tab

class IssueFragment : Fragment() {
    private val viewModel: NewsViewModel by viewModels()
    private lateinit var newsRecyclerView: RecyclerView
    companion object{
        const val TAG : String = "로그"
        fun newInstance() : IssueFragment{
            return IssueFragment()
        }
    }

    // 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "NewsFragment -onCreate() called")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "NewsFragment -onAttach() called")
    }

    // 뷰가 생성됐을 때
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_issue,container,false)

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView(view)
        LiveData(view)
        btnEvent(view)
    }
    fun LiveData(view:View){
        viewModel.newsLiveData.observe(viewLifecycleOwner, Observer {
            var result = it.items
            val mAdapter = this!!.activity?.let { it1 -> NewsAdapter(result, it1) }
            newsRecyclerView.adapter = mAdapter
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