package com.uni.onclicklgubus.ui.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.uni.onclicklgubus.R
import com.uni.onclicklgubus.base.BaseActivity
import com.uni.onclicklgubus.databinding.ActivityAdminTrackingBussBinding
import com.uni.onclicklgubus.firebase.DataBase
import com.uni.onclicklgubus.model.Bus
import com.uni.onclicklgubus.model.Driver
import kotlin.math.ln

class AdminTrackingBuss : BaseActivity<ActivityAdminTrackingBussBinding>(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var mMarker: Marker? = null
    override fun getViewBinding(): ActivityAdminTrackingBussBinding =
        ActivityAdminTrackingBussBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        init()
        setListener()

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        mMap.uiSettings.apply {

            isZoomControlsEnabled = true
            isZoomGesturesEnabled = false
        }

        getBusOnRoadsFromServer()

        /* // Add a marker in Sydney and move the camera
         val sydney = LatLng(-34.0, 151.0)
         mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
         mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/
    }

    override fun init() {

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun setListener() {
    }

    private fun getBusOnRoadsFromServer() {
        mViewBinding.apply {
            val query: Query = DataBase.BUS_TRACKING_DB_REF

            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var latLng: LatLng? = null

                    if (mMarker != null) {
                        mMarker!!.remove();

                    }
                    for (b in dataSnapshot.children) {
                        val bus = b.getValue(Bus::class.java)!!

                        bus.apply {
                            latLng = LatLng(lat!!, lng!!)

                            //For removing multiplicity of marker
                            if (mMarker != null) {
                                //mMap.setMaxZoomPreference(20);
                                moveCamera(
                                    latLng!!,
                                    12.0f,
                                    driverName.toString(),
                                    busNumber.toString()
                                );
                            } else {
                                //mMap.setMaxZoomPreference(20);
                                moveCamera(
                                    latLng!!,
                                    12.0f,
                                    driverName.toString(),
                                    busNumber.toString()
                                );
                            }
                            /*mMarker = mMap.addMarker(
                                MarkerOptions()
                                    .position(latLng!!)
                                    .title(driverName)
                                    .snippet(busNumber)
                                    .icon(
                                        BitmapDescriptorFactory.fromResource(
                                            R.drawable.ic_marker
                                        )
                                    )

                            )*/
                        }

                        /* mMap.moveCamera(
                             CameraUpdateFactory.newLatLngZoom(
                                 latLng!!,
                                 17f
                             )
                         )*/

                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    showSnackBar(databaseError.message)
                }
            })

        }
    }

    private fun moveCamera(latLng: LatLng, zoom: Float, title: String, snippet: String) {
        //Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
        mMarker = mMap.addMarker(
            MarkerOptions().position(latLng)
                .title(title)
                .snippet(snippet)
                .icon(
                    BitmapDescriptorFactory.fromResource(
                        R.drawable.ic_marker
                    )
                )
        )
    }
}