package com.uni.onclicklgubus.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.uni.onclicklgubus.base.BaseActivity
import com.uni.onclicklgubus.databinding.ActivityWelcomeBinding
import com.uni.onclicklgubus.utils.Constants


class WelcomeActivity : BaseActivity<ActivityWelcomeBinding>() {
    override fun getViewBinding(): ActivityWelcomeBinding =
        ActivityWelcomeBinding.inflate(layoutInflater)

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

        mViewBinding.apply {
            btnAdmin.setOnClickListener {
                goToNextActivity(1)
            }
            btnStudent.setOnClickListener {
                goToNextActivity(2)
            }
            btnDriver.setOnClickListener {
                goToNextActivity(3)
            }
        }
    }

    private fun goToNextActivity(loginOption: Int) {
        val intent = Intent(baseContext, LoginActivity::class.java)
        intent.putExtra(Constants.BUNDLE_DATA, loginOption)
        startActivity(intent)
    }
}