package com.uni.onclicklgubus.ui.admin

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.uni.onclicklgubus.adapter.DriversListAdapter
import com.uni.onclicklgubus.base.BaseActivity
import com.uni.onclicklgubus.databinding.ActivityDriverListBinding
import com.uni.onclicklgubus.firebase.DataBase
import com.uni.onclicklgubus.model.Driver

class DriverListActivity : BaseActivity<ActivityDriverListBinding>() {

    var mDriversList = mutableListOf<Driver>()

    private val adapter by lazy {
        DriversListAdapter()
    }

    override fun getViewBinding(): ActivityDriverListBinding =
        ActivityDriverListBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        init()
        setListener()
        getDriverFromServer()
    }

    override fun init() {
        mViewBinding.apply {
            rvDrivers.adapter = adapter
        }
    }

    private fun getDriverFromServer() {
        mViewBinding.apply {
            val driverEmail: Query = DataBase.DRIVER_DB_REF

            driverEmail.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    setVisibility(rvDrivers, pb)
                    mDriversList.clear()
                    for (drivers in dataSnapshot.children) {
                        val driver = drivers.getValue(Driver::class.java)!!
                        mDriversList.add(driver)
                    }
                    adapter.addItems(mDriversList)

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    setVisibility(rvDrivers, pb)
                    showSnackBar(databaseError.message)
                }
            })

        }
    }

    override fun setListener() {
    }

    fun goToTrackingBussActivity(view: View) {

        navigateToNextActivity(AdminTrackingBuss::class.java)
    }
}