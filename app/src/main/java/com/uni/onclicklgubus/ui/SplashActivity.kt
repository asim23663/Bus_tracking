package com.uni.onclicklgubus.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatDelegate
import com.uni.onclicklgubus.R
import com.uni.onclicklgubus.base.BaseActivity
import com.uni.onclicklgubus.databinding.ActivitySplashBinding
import com.uni.onclicklgubus.sharedPref.SharedPrefHelper
import com.uni.onclicklgubus.ui.admin.AdminDashboardActivity
import com.uni.onclicklgubus.ui.driver.DriverDashboardActivity
import com.uni.onclicklgubus.ui.student.StudentDashboardActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    private val SPLASH_DISPLAY_LENGTH = 3000L

    override fun getViewBinding(): ActivitySplashBinding =
        ActivitySplashBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        splashScreenCodeWithFadeAnimation()
    }

    private fun splashScreenCodeWithFadeAnimation() {
        val animation1 = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        mViewBinding.splashIvLogo.startAnimation(animation1)

        Handler(Looper.getMainLooper()).postDelayed({
            try {

                SharedPrefHelper.instance!!.apply {
                    when {
                        isAdminLogin -> navigateToNextActivity(AdminDashboardActivity::class.java)
                        isStudentLogin -> navigateToNextActivity(StudentDashboardActivity::class.java)
                        isDriverLogin -> navigateToNextActivity(DriverDashboardActivity::class.java)
                        else -> navigateAndClearBackStack(WelcomeActivity::class.java)
                    }
                }

            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }, SPLASH_DISPLAY_LENGTH)
    }

    override fun init() {
    }

    override fun setListener() {
    }
}