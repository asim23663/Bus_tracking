@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.uni.onclicklgubus.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.uni.onclicklgubus.model.User
import com.uni.onclicklgubus.sharedPref.SharedPrefHelper
import com.uni.onclicklgubus.utils.Utils

object DataBase {


    private val CURRENT_UID = FirebaseAuth.getInstance().currentUser.uid
    private val DATA_BASE = FirebaseDatabase.getInstance()

    private const val USERS = "users"
    private const val DRIVERS = "drivers"
    private const val BUS = "busTracking"

    private val CURRENT_USER_DB_REF = DATA_BASE.getReference(USERS).child(CURRENT_UID)

    val DRIVER_DB_REF = DATA_BASE.getReference(DRIVERS)
    val BUS_DB_REF = DATA_BASE.getReference(BUS)


//    val CURRENT_USER_TASKS_DB_REF = DATA_BASE.getReference(TASK).child(CURRENT_UID)


    fun updateUserData() {
        CURRENT_USER_DB_REF
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //query here (according to days)
                    val user = dataSnapshot.getValue<User>()
//                    SharedPrefHelper.instance!!.saveLoginUser(user!!)
                }

                override fun onCancelled(error: DatabaseError) {
                    Utils.showToast(error.toException().toString())
                }
            })
    }
}