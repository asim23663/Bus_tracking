package com.uni.onclicklgubus.ui.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.uni.onclicklgubus.R
import com.uni.onclicklgubus.adapter.DriversListAdapter
import com.uni.onclicklgubus.adapter.StudentsListAdapter
import com.uni.onclicklgubus.base.BaseActivity
import com.uni.onclicklgubus.databinding.ActivityStudentListBinding
import com.uni.onclicklgubus.firebase.DataBase
import com.uni.onclicklgubus.model.Driver
import com.uni.onclicklgubus.model.Student
import com.uni.onclicklgubus.utils.Constants

class StudentListActivity : BaseActivity<ActivityStudentListBinding>() {

    var mStudentsList = mutableListOf<Student>()

    private val adapter by lazy {
        StudentsListAdapter()
    }

    override fun getViewBinding(): ActivityStudentListBinding =
        ActivityStudentListBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        init()
        setListener()
        getStudentFromServer()
    }

    override fun init() {
        mViewBinding.apply {
            rvStudent.adapter = adapter
        }
    }

    override fun setListener() {

        adapter.listener = { view, item, position ->
            when (view.id) {
                R.id.cv_driver -> {
                    showDialog(item, position)
                }
            }
        }
    }

    private fun showDialog(driver: Student, position: Int) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Select")
            .setItems(Constants.OPTIONS) { dialog, which ->
                when {
                    Constants.OPTIONS[which] == Constants.OPTIONS[0] -> {
                        val intent = Intent(this, AddNewStudentActivity::class.java)
                        intent.putExtra(Constants.BUNDLE_DATA, driver)
                        startActivity(intent)

                    }
                    /*  OPTIONS[which] == OPTIONS[1] -> {

                          val bundle = bundleOf()
                          bundle.putParcelable(Constants.BUNDLE_DATA, post)
                          findNavController().navigate(R.id.action_my_ads_to_addPostFragment, bundle)

                      }*/
                    Constants.OPTIONS[which] == Constants.OPTIONS[1] -> {
                        showToast("Deleted Successfully")
                        DataBase.DRIVER_DB_REF.child(driver.uid.toString()).removeValue()
                        adapter.removeItems(position)
                    }
                }
            }
            .setCancelable(true)
            .show()
    }


    private fun getStudentFromServer() {
        mViewBinding.apply {
            val query: Query = DataBase.STUDENT_DB_REF

            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    setVisibility(rvStudent, pb)
                    mStudentsList.clear()
                    for (students in dataSnapshot.children) {
                        val student = students.getValue(Student::class.java)!!
                        mStudentsList.add(student)
                    }
                    adapter.addItems(mStudentsList)

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    setVisibility(rvStudent, pb)
                    showSnackBar(databaseError.message)
                }
            })

        }
    }

}