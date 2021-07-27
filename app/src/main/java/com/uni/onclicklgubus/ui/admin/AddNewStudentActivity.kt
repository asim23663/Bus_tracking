package com.uni.onclicklgubus.ui.admin

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatDelegate
import com.uni.onclicklgubus.R
import com.uni.onclicklgubus.base.BaseActivity
import com.uni.onclicklgubus.databinding.ActivityAddNewStudentBinding
import com.uni.onclicklgubus.utils.Utils

class AddNewStudentActivity : BaseActivity<ActivityAddNewStudentBinding>() {
    override fun getViewBinding(): ActivityAddNewStudentBinding =
        ActivityAddNewStudentBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        init()
        setListener()
    }

    override fun init() {

        mViewBinding.apply {
            val items = listOf("Male", "Female", "Other")
            val adapter = ArrayAdapter(this@AddNewStudentActivity, R.layout.list_item, items)
            (etGender as? AutoCompleteTextView)?.setAdapter(adapter)
        }

    }

    override fun setListener() {
        mViewBinding.apply {
            etDob.setOnClickListener {
                Utils.selectDate(this@AddNewStudentActivity, etDob)
//            selectCalendarDate()
            }
        }
    }
}