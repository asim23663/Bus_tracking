package com.uni.onclicklgubus.ui.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.uni.onclicklgubus.R
import com.uni.onclicklgubus.adapter.DriversListAdapter
import com.uni.onclicklgubus.base.BaseActivity
import com.uni.onclicklgubus.databinding.ActivityDriverListBinding
import com.uni.onclicklgubus.firebase.DataBase
import com.uni.onclicklgubus.model.Driver
import com.uni.onclicklgubus.utils.Constants
import com.uni.onclicklgubus.utils.Constants.OPTIONS

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

        adapter.listener = { view, item, position ->
            when (view.id) {
                R.id.cv_driver -> {
                    showDialog(item, position)
                }
            }
        }
    }

    fun goToTrackingBussActivity(view: View) {

        navigateToNextActivity(AdminTrackingBuss::class.java)
    }

    private fun showDialog(driver: Driver, position: Int) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Select")
            .setItems(OPTIONS) { dialog, which ->
                when {
                    OPTIONS[which] == OPTIONS[0] -> {
                        val intent = Intent(this, AddNewDriverActivity::class.java)
                        intent.putExtra(Constants.BUNDLE_DATA, driver)
                        startActivity(intent)

                    }
                    /*  OPTIONS[which] == OPTIONS[1] -> {

                          val bundle = bundleOf()
                          bundle.putParcelable(Constants.BUNDLE_DATA, post)
                          findNavController().navigate(R.id.action_my_ads_to_addPostFragment, bundle)

                      }*/
                    OPTIONS[which] == OPTIONS[1] -> {
                        showToast("Deleted Successfully")
                        DataBase.DRIVER_DB_REF.child(driver.uid.toString()).removeValue()
                        adapter.removeItems(position)
                    }
                }
            }
            .setCancelable(true)
            .show()
    }

}