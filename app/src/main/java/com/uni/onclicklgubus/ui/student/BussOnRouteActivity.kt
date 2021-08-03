package com.uni.onclicklgubus.ui.student

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.uni.onclicklgubus.R
import com.uni.onclicklgubus.adapter.BussOnRouteListAdapter
import com.uni.onclicklgubus.adapter.DriversListAdapter
import com.uni.onclicklgubus.base.BaseActivity
import com.uni.onclicklgubus.databinding.ActivityBussOnRouteBinding
import com.uni.onclicklgubus.firebase.DataBase
import com.uni.onclicklgubus.model.Bus
import com.uni.onclicklgubus.model.Driver
import com.uni.onclicklgubus.ui.LoginActivity
import com.uni.onclicklgubus.utils.Constants
import com.uni.onclicklgubus.utils.Constants.BUNDLE_DATA

class BussOnRouteActivity : BaseActivity<ActivityBussOnRouteBinding>() {

    var mBusList = mutableListOf<Bus>()

    private val adapter by lazy {
        BussOnRouteListAdapter()
    }

    override fun getViewBinding(): ActivityBussOnRouteBinding =
        ActivityBussOnRouteBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        init()
        setListener()
        getBussOnRouteFromServer()
    }

    private fun getBussOnRouteFromServer() {
        mViewBinding.apply {
            val driverEmail: Query = DataBase.BUS_TRACKING_DB_REF

            driverEmail.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    setVisibility(rvBus, pb)
                    mBusList.clear()
                    for (d in dataSnapshot.children) {
                        val bus = d.getValue(Bus::class.java)!!
                        mBusList.add(bus)
                    }
                    adapter.addItems(mBusList)

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    setVisibility(rvBus, pb)
                    showSnackBar(databaseError.message)
                }
            })

        }


    }

    override fun init() {
        mViewBinding.apply {
            rvBus.adapter = adapter
        }

    }

    override fun setListener() {

        adapter.listener = { view, item, position ->

            when (view.id) {
                R.id.cv_bus -> {
                    val intent = Intent(baseContext, BusTrackingActivity::class.java)
                    intent.putExtra(BUNDLE_DATA, item)
                    startActivity(intent)
                }
            }


        }
    }
}