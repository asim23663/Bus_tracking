package com.uni.onclicklgubus.ui.student

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.uni.onclicklgubus.R
import com.uni.onclicklgubus.base.BaseActivity
import com.uni.onclicklgubus.databinding.ActivityStudentDashboardBinding
import com.uni.onclicklgubus.sharedPref.SharedPref
import com.uni.onclicklgubus.sharedPref.SharedPrefHelper
import com.uni.onclicklgubus.ui.WelcomeActivity

class StudentDashboardActivity : BaseActivity<ActivityStudentDashboardBinding>() {
    override fun getViewBinding(): ActivityStudentDashboardBinding =
        ActivityStudentDashboardBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        init()
        setListener()
    }

    override fun init() {

        mViewBinding.apply {

            SharedPrefHelper.instance!!.getStudentData.apply {
                tvName.text = name
                tvFather.text = fatherName
                tvRollNumber.text = rollNumber
                tvDepartment.text = department
                tvBatch.text = batch
                tvCAddress.text = currentAddress
                tvPAddress.text = permanentAddress
                tvContactNumber.text = contactNumber
                tvEmail.text = email
                tvGender.text = gender
                tvDob.text = dob

            }
        }


    }

    override fun setListener() {
        mViewBinding.apply {
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
                FirebaseAuth.getInstance().signOut();
                navigateAndClearBackStack(WelcomeActivity::class.java)
            }
            .setNegativeButton(
                "Cancel"
            ) { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }.show()
    }

    fun goToTrackingBussOnRoute(view: View) {

        navigateToNextActivity(BussOnRouteActivity::class.java)
    }

    fun submitFeeOnline(view: View) {
        navigateToNextActivity(SubmitFeeOnlineActivity::class.java)

    }

}