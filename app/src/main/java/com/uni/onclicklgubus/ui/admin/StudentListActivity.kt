package com.uni.onclicklgubus.ui.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.uni.onclicklgubus.base.BaseActivity
import com.uni.onclicklgubus.databinding.ActivityStudentListBinding

class StudentListActivity : BaseActivity<ActivityStudentListBinding>() {
    override fun getViewBinding(): ActivityStudentListBinding =
        ActivityStudentListBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        init()
        setListener()
    }

    override fun init() {
    }

    override fun setListener() {
    }
}