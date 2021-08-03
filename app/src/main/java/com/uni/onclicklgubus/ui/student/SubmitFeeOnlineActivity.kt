package com.uni.onclicklgubus.ui.student

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.uni.onclicklgubus.base.BaseActivity
import com.uni.onclicklgubus.databinding.ActivitySubmitFeeOnlineBinding

class SubmitFeeOnlineActivity : BaseActivity<ActivitySubmitFeeOnlineBinding>() {
    override fun getViewBinding(): ActivitySubmitFeeOnlineBinding =
        ActivitySubmitFeeOnlineBinding.inflate(layoutInflater)

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