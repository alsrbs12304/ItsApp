package com.example.itsapp.view.activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.itsapp.R
import com.example.itsapp.view.fragment.HomeFragment
import com.example.itsapp.view.fragment.IssueFragment
import com.example.itsapp.view.fragment.MyPageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var homeFragment: HomeFragment
    private lateinit var myPageFragment: MyPageFragment
    private lateinit var issueFragment: IssueFragment
    private var backPressedTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        bottom_navigation_view.setOnNavigationItemSelectedListener(this)

        // onCreate되면서 홈프래그먼트를 add로 바로 띄워준다.
        homeFragment = HomeFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.container,homeFragment).commit()
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when(p0.itemId) {
            R.id.action_home -> {
                homeFragment = HomeFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.container,homeFragment).commit()
            }
            R.id.action_news ->{
                issueFragment = IssueFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.container,issueFragment).commit()
            }
            R.id.action_mypage ->{
                myPageFragment = MyPageFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.container,myPageFragment).commit()
            }
        }
        return true
    }

    // 뒤로가기 버튼 2번 클릭 시 앱 종료
    override fun onBackPressed() {
        Log.d("TAG", "뒤로가기")

        // 2초내 다시 클릭하면 앱 종료
        if (System.currentTimeMillis() - backPressedTime < 2000) {
            finish()
            return
        }
        // 처음 클릭 메시지
        Snackbar.make(home_activity, "'뒤로' 버튼을 한번 더 누르시면 앱이 종료됩니다.", Snackbar.LENGTH_SHORT).show()
        backPressedTime = System.currentTimeMillis()
    }
}