package com.example.cupet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.home_include_drawer.*
import kotlinx.android.synthetic.main.toolbar_item.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_include_drawer)

        drawer.setOnClickListener {
            home_drawer_layout.openDrawer((GravityCompat.START))
        }

        nav_join.setOnClickListener {
            //버튼1 클릭 시
        }

        nav_cal.setOnClickListener {
            //버튼2 클릭 시
        }

        nav_post.setOnClickListener {
            //버튼3 클릭 시
        }

        nav_bookmark.setOnClickListener {
            //버튼3 클릭 시
        }

        nav_mypost.setOnClickListener {
            //버튼3 클릭 시
        }
    }
}