package com.uni.onclicklgubus.ui.admin

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.uni.onclicklgubus.R
import com.uni.onclicklgubus.base.BaseActivity
import com.uni.onclicklgubus.databinding.ActivityAdminDashboardBinding
import com.uni.onclicklgubus.sharedPref.SharedPref
import com.uni.onclicklgubus.sharedPref.SharedPrefHelper
import com.uni.onclicklgubus.ui.WelcomeActivity

class AdminDashboardActivity : BaseActivity<ActivityAdminDashboardBinding>() {


    override fun getViewBinding(): ActivityAdminDashboardBinding =
        ActivityAdminDashboardBinding.inflate(layoutInflater)

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

            btnAddNewDriver.setOnClickListener {
                navigateToNextActivity(AddNewDriverActivity::class.java)
            }

            btnSeeDriver.setOnClickListener {
                navigateToNextActivity(DriverListActivity::class.java)
            }
            btnAddNewStudent.setOnClickListener {
                navigateToNextActivity(AddNewStudentActivity::class.java)
            }

            btnSeeStudent.setOnClickListener {
                navigateToNextActivity(StudentListActivity::class.java)
            }

            ivLogout.setOnClickListener {
                doLogout()
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun doLogout() {
        MaterialAlertDialogBuilder(mViewBinding.root.context)
            .setTitle(resources.getString(R.string.app_name))
            .setMessage(R.string.str_logout_msg)
            .setIcon(resources.getDrawable(R.mipmap.ic_app_icon, null))
            .setPositiveButton("Yes") { dialog: DialogInterface?, whichButton: Int ->
                SharedPref.instance!!.clearSharedPref()
                navigateAndClearBackStack(WelcomeActivity::class.java)
            }
            .setNegativeButton(
                "Cancel"
            ) { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }.show()
    }
}