package com.uni.onclicklgubus.ui.student

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.uni.onclicklgubus.R
import com.uni.onclicklgubus.base.BaseActivity
import com.uni.onclicklgubus.databinding.ActivityBusTrackingBinding
import com.uni.onclicklgubus.firebase.DataBase
import com.uni.onclicklgubus.model.Bus
import com.uni.onclicklgubus.model.Student
import com.uni.onclicklgubus.sharedPref.SharedPrefHelper
import com.uni.onclicklgubus.ui.driver.DriverDashboardActivity
import com.uni.onclicklgubus.utils.Constants
import java.text.DecimalFormat

class BusTrackingActivity : BaseActivity<ActivityBusTrackingBinding>(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    //    private lateinit var auth: FirebaseAuth
    private var mMarker: Marker? = null
    var distance = 0.0
    var currentLatLng: LatLng? = null

    private var fusedLocationProvider: FusedLocationProviderClient? = null

    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 40
        fastestInterval = 20
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        maxWaitTime = 6000
    }

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                //The last location in the list is the newest
                val location = locationList.last()



                currentLatLng = LatLng(
                    location.latitude,
                    location.longitude
                ) //Store these lat lng values somewhere. These should be constant.

//                val cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(
//                    coordinate, 12f
//                )
//                mMap.animateCamera(cameraUpdateFactory)

//                showToast("Got Location:  ")

            }
        }
    }

    // The Fused Location Provider provides access to location APIs.
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(applicationContext)
    }

    override fun getViewBinding(): ActivityBusTrackingBinding =
        ActivityBusTrackingBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        initLocationMap()
        init()
        setListener()
    }


    override fun init() {

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val busData = intent.getSerializableExtra(Constants.BUNDLE_DATA) as Bus

        mViewBinding.apply {
            busData.apply {
                tvDriverName.text = driverName
                tvBusNmbr.text = busNumber

                getBusLocation(busNumber!!)

            }

        }

    }

    override fun setListener() {
    }

    private fun initLocationMap() {
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)

        checkLocationPermission()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMapToolbarEnabled = true
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mMap.isMyLocationEnabled = true


        mMap.uiSettings.apply {

            isZoomControlsEnabled = true
            isZoomGesturesEnabled = false
        }


    }

    private fun getBusLocation(busNumber: String) {

        DataBase.BUS_TRACKING_DB_REF
            .child(busNumber)
            .addValueEventListener(object :
                ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var latLng: LatLng?

                    try {
                        val bus = dataSnapshot.getValue(Bus::class.java)!!

                        bus.apply {
                            latLng = LatLng(lat!!, lng!!)


                            try {
                                distance = calculationByDistance(currentLatLng!!, latLng!!);
                                val d = "Distance: " + distance.toInt() + " Km"
                                mViewBinding.tvTime.text = "${distance.toInt()} KM"
                            } catch (e: NullPointerException) {
                                Log.d(TAG, "NullPointerException")
                            }


                            //For removing multiplicity of marker
                            if (mMarker != null) {
                                mMarker!!.remove();
                                //mMap.setMaxZoomPreference(20);
                                moveCamera(latLng!!, busNumber);
                            } else {
                                //mMap.setMaxZoomPreference(20);
                                moveCamera(latLng!!, busNumber);
                            }
                        }
                    } catch (e: NullPointerException) {
                        showSnackBar("Something went wrong")
                    }


                }

                override fun onCancelled(databaseError: DatabaseError) {
                    showSnackBar(databaseError.message)
                }
            })
    }

    private fun moveCamera(latLng: LatLng, title: String) {
        //Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f))
        mMarker = mMap.addMarker(
            MarkerOptions().position(latLng)
                .title(title)
                .icon(
                    BitmapDescriptorFactory.fromResource(
                        R.drawable.ic_marker
                    )
                )
        )
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        //Prompt the user once explanation has been shown
                        requestLocationPermission()
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                requestLocationPermission()
            }
        }
    }

    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        fusedLocationProvider?.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.getMainLooper()
                        )
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    showToast("permission denied")
                }
                return
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {

            fusedLocationProvider?.removeLocationUpdates(locationCallback)
        }
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {

            fusedLocationProvider?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    fun calculationByDistance(StartP: LatLng, EndP: LatLng): Double {
        val Radius = 6371 // radius of earth in Km
        val lat1 = StartP.latitude
        val lat2 = EndP.latitude
        val lon1 = StartP.longitude
        val lon2 = EndP.longitude
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = (Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + (Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2)))
        val c = 2 * Math.asin(Math.sqrt(a))
        val valueResult = Radius * c
        val km = valueResult / 1
        val newFormat = DecimalFormat("####")
        val kmInDec: Int = Integer.valueOf(newFormat.format(km))
        val meter = valueResult % 1000
        val meterInDec: Int = Integer.valueOf(newFormat.format(meter))


        Log.i(
            "Radius Value", "" + valueResult + "   KM  " + kmInDec
                    + " Meter   " + meterInDec
        )
        return Radius * c
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        private const val TAG = "BusTrackingActivity"
    }
}