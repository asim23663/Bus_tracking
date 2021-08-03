package com.uni.onclicklgubus.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.uni.onclicklgubus.R
import com.uni.onclicklgubus.base.BaseActivity
import com.uni.onclicklgubus.databinding.ActivityLoginBinding
import com.uni.onclicklgubus.firebase.DataBase
import com.uni.onclicklgubus.model.Driver
import com.uni.onclicklgubus.model.Student
import com.uni.onclicklgubus.sharedPref.SharedPrefHelper
import com.uni.onclicklgubus.ui.admin.AdminDashboardActivity
import com.uni.onclicklgubus.ui.driver.DriverDashboardActivity
import com.uni.onclicklgubus.ui.student.StudentDashboardActivity
import com.uni.onclicklgubus.utils.Constants


class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private lateinit var auth: FirebaseAuth

    override fun getViewBinding(): ActivityLoginBinding =
        ActivityLoginBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        init()
        setListener()
    }

    @SuppressLint("SetTextI18n")
    override fun init() {

        auth = FirebaseAuth.getInstance()

        val loginOption = intent.getIntExtra(Constants.BUNDLE_DATA, 0)

        mViewBinding.apply {
            when (loginOption) {
                1 -> {
                    tvWelcome.text = "Welcome, Admin"
                    admin.parentLayout.visibility = View.VISIBLE
                }
                2 -> {
                    tvWelcome.text = "Welcome, Student"
                    student.parentLayout.visibility = View.VISIBLE
                }
                3 -> {
                    tvWelcome.text = "Welcome, Driver"
                    driver.parentLayout.visibility = View.VISIBLE
                }
            }
        }

    }

    override fun setListener() {

        mViewBinding.apply {

            admin.apply {
                btnLogin.setOnClickListener {
                    when {
                        isEmpty(etEmail) -> showSnackBarError("Email is not empty")
                        isEmpty(etPassword) -> showSnackBarError("Password is not empty")
                        isEmailValid(getText(etEmail)) -> showSnackBarError("Email is not valid")
                        else -> {
                            setVisibility(pb, btnLogin)
                            auth.signInWithEmailAndPassword(getText(etEmail), getText(etPassword))
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val uid = auth.currentUser!!.uid
                                        SharedPrefHelper.instance!!.saveAdminLogin(true)
                                        navigateAndClearBackStack(AdminDashboardActivity::class.java)
                                    } else {
                                        setVisibility(btnLogin, pb)
                                        try {
                                            throw task.exception!!
                                        } catch (e: FirebaseAuthWeakPasswordException) {
                                            showSnackBar(getString(R.string.error_weak_password))
                                        } catch (e: FirebaseAuthInvalidCredentialsException) {
                                            showSnackBar(getString(R.string.error_invalid_email))
                                        } catch (e: FirebaseAuthUserCollisionException) {
                                            showSnackBar(getString(R.string.error_user_exists))
                                        } catch (e: Exception) {
                                            Log.e(TAG, e.message!!)
                                            showSnackBar(e.message!!)
                                        }

                                    }
                                }
                        }
                    }


                }
            }

            driver.apply {
                btnLogin.setOnClickListener {
                    FirebaseAuth.getInstance().signOut();
                    when {
                        isEmpty(etEmail) -> showSnackBarError("Email is not empty")
                        isEmpty(etPassword) -> showSnackBarError("Password is not empty")
                        isEmailValid(getText(etEmail)) -> showSnackBarError("Email is not valid")
                        isEmpty(etBusNumb) -> showSnackBarError("Bus number is required")
                        getText(etPassword).length < 6 -> showSnackBarError("Please enter 6 digits at least")
                        else -> {
                            setVisibility(pb, btnLogin)

                            Log.d(TAG, "Email: ${getText(etEmail)}")
                            Log.d(TAG, "Password: ${getText(etPassword)}")
                            auth.signInWithEmailAndPassword(getText(etEmail), getText(etPassword))
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        setVisibility(btnLogin, pb)

                                        val uid = auth.currentUser!!.uid

                                        checkDriverIfExist()

                                    } else {
                                        setVisibility(btnLogin, pb)
                                        try {
                                            throw task.exception!!
                                        } catch (e: FirebaseAuthWeakPasswordException) {
                                            showSnackBar(getString(R.string.error_weak_password))
                                        } catch (e: FirebaseAuthInvalidCredentialsException) {


                                            Log.d(
                                                TAG,
                                                "FirebaseAuthInvalidCredentialsException: ${e.errorCode}"
                                            )
                                            Log.d(
                                                TAG,
                                                "FirebaseAuthInvalidCredentialsException: ${e.localizedMessage}"
                                            )
                                            Log.d(
                                                TAG,
                                                "FirebaseAuthInvalidCredentialsException: ${e.message}"
                                            )

//                                            showSnackBar(getString(R.string.error_invalid_email))
                                            showSnackBar(e.message)
                                        } catch (e: FirebaseAuthUserCollisionException) {
                                            showSnackBar(getString(R.string.error_user_exists))
                                        } catch (e: Exception) {
                                            Log.e(TAG, e.message!!)
                                            showSnackBar(e.message!!)
                                        }

                                    }
                                }
                        }
                    }


                }
            }

            student.apply {
                btnLogin.setOnClickListener {
                    when {
                        isEmpty(etEmail) -> showSnackBarError("Email is not empty")
                        isEmpty(etPassword) -> showSnackBarError("Password is not empty")
                        isEmailValid(getText(etEmail)) -> showSnackBarError("Email is not valid")
                        else -> {
                            setVisibility(pb, btnLogin)
                            auth.signInWithEmailAndPassword(getText(etEmail), getText(etPassword))
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        setVisibility(btnLogin, pb)

                                        val uid = auth.currentUser!!.uid

                                        checkStudentRollNumberIfExist()

                                    } else {
                                        setVisibility(btnLogin, pb)
                                        try {
                                            throw task.exception!!
                                        } catch (e: FirebaseAuthWeakPasswordException) {
                                            showSnackBar(getString(R.string.error_weak_password))
                                        } catch (e: FirebaseAuthInvalidCredentialsException) {
                                            showSnackBar(getString(R.string.error_invalid_email))
                                        } catch (e: FirebaseAuthUserCollisionException) {
                                            showSnackBar(getString(R.string.error_user_exists))
                                        } catch (e: Exception) {
                                            Log.e(TAG, e.message!!)
                                            showSnackBar(e.message!!)
                                        }
                                    }
                                }
                        }
                    }
                }
            }

        }

    }

    private fun checkStudentRollNumberIfExist() {


        mViewBinding.student.apply {
            val query: Query = DataBase.STUDENT_DB_REF
                .orderByChild("rollNumber")
                .equalTo(getText(etRollNum))

            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                    for (d in dataSnapshot.children) {

                        val student = d.getValue(Student::class.java)!!

                        if (student.rollNumber == getText(etRollNum) && student.email == getText(etEmail)) {
                            SharedPrefHelper.instance!!.saveStudentLogin(true)
                            SharedPrefHelper.instance!!.saveStudentData(student)
                            navigateAndClearBackStack(StudentDashboardActivity::class.java)
                        } else {
                            showSnackBar("Roll number number is not Matches")
                        }


                    }




                }

                override fun onCancelled(@NonNull databaseError: DatabaseError) {}
            })
        }



    }

    private fun checkDriverIfExist() {

        mViewBinding.driver.apply {
            val driverEmail: Query = DataBase.DRIVER_DB_REF.orderByChild("email")
                .equalTo(getText(etEmail))

            driverEmail.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (drivers in dataSnapshot.children) {

                        val driver = drivers.getValue(Driver::class.java)!!

                        if (driver.busNumber == getText(etBusNumb)) {
                            SharedPrefHelper.instance!!.saveDriverLogin(true)
                            SharedPrefHelper.instance!!.saveDriverData(driver)
                            navigateAndClearBackStack(
                                DriverDashboardActivity::class.java
                            )
                            Log.i(
                                TAG,
                                drivers.child("name").getValue(String::class.java)!!
                            )
                        } else {
                            showSnackBar("Bus number is not Matches")
                        }


                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    showSnackBar(databaseError.message)
                }
            })
        }


    }

    companion object {
        private const val TAG = "LoginActivity--"
    }
}