package com.example.itsapp.view.fragment

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
import com.example.itsapp.view.adapter.BlogAdapter
import com.example.itsapp.viewmodel.NewsViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_blog.tips_tab

class BlogFragment : Fragment() {

    private val viewModel: NewsViewModel by viewModels()
    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var blogRecyclerView: RecyclerView
    private var userAge:String =""
    private var userSex:String =""
    private var userJob:String = ""
    companion object{
        fun newInstance() : BlogFragment {
            return BlogFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView(view)
        LiveData(view)
        btnEvent()
    }
    fun LiveData(view:View){
        viewModel.blogLiveData.observe(viewLifecycleOwner, Observer {
            var result = it.items
            val mAdapter = this!!.activity?.let { it1 -> BlogAdapter(result, it1) }
            blogRecyclerView.adapter = mAdapter
        })
    }
    fun recyclerView(view:View){
        blogRecyclerView = view.findViewById(R.id.blog_rv)
        var BlogLayoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL,false)
        blogRecyclerView.layoutManager = BlogLayoutManager
        viewModel.searchReadBlog("노트북",1,100)
    }
    fun btnEvent(){
        tips_tab.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabSelected(p0: TabLayout.Tab?) {
                val position = p0?.position
                when(position){
                    0 -> {
                        viewModel.searchReadBlog("맥북 리뷰",1,100)
                    }
                    1 -> {
                        viewModel.searchReadBlog("삼성 노트북 리뷰",1,100)
                    }
                    2 -> {
                        viewModel.searchReadBlog("LG 노트북 리뷰",1,100)
                    }
                    3 -> {
                        viewModel.searchReadBlog("ASUS 노트북 리뷰",1,100)
                    }
                    4 -> {
                        viewModel.searchReadBlog("DELL 노트북 리뷰",1,100)
                    }
                    5 -> {
                        viewModel.searchReadBlog("LENOVO 노트북 리뷰",1,100)
                    }
                }
            }
        })
    }
}