package com.uni.onclicklgubus.ui.driver

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.uni.onclicklgubus.R
import com.uni.onclicklgubus.base.BaseActivity
import com.uni.onclicklgubus.databinding.ActivityDriverDashboardBinding
import com.uni.onclicklgubus.sharedPref.SharedPref
import com.uni.onclicklgubus.ui.WelcomeActivity

class DriverDashboardActivity : BaseActivity<ActivityDriverDashboardBinding>(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    // The Fused Location Provider provides access to location APIs.
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(applicationContext)
    }

    private lateinit var auth: FirebaseAuth

    override fun getViewBinding(): ActivityDriverDashboardBinding =
        ActivityDriverDashboardBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        init()
        setListener()
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(31.5204, 74.3587)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Lahore"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun init() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
}