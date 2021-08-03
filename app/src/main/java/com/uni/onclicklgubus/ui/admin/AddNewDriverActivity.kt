package com.uni.onclicklgubus.ui.admin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.uni.onclicklgubus.R
import com.uni.onclicklgubus.base.BaseActivity
import com.uni.onclicklgubus.databinding.ActivityAddNewDriverBinding
import com.uni.onclicklgubus.firebase.DataBase.DRIVER_DB_REF
import com.uni.onclicklgubus.model.Bus
import com.uni.onclicklgubus.model.Driver
import com.uni.onclicklgubus.utils.Constants
import com.uni.onclicklgubus.utils.Utils

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AddNewDriverActivity : BaseActivity<ActivityAddNewDriverBinding>() {

    private lateinit var auth: FirebaseAuth
    private var driverData: Driver? = null
    override fun getViewBinding(): ActivityAddNewDriverBinding =
        ActivityAddNewDriverBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        init()
        setListener()
    }

    override fun init() {
        auth = FirebaseAuth.getInstance()


        mViewBinding.apply {
            if (intent.extras != null) {
                supportActionBar!!.title = "Edit Driver";
                driverData = intent.getSerializableExtra(Constants.BUNDLE_DATA) as Driver
                driverData!!.apply {
                    etName.setText(name)
                    etDob.setText(dob)
                    etExperience.setText(experience)
                    etNumber.setText(phoneNumber)
                    etDrivingLicenceNumber.setText(drivingLicenceNumber)
                    etDrivingLicenceExpireDate.setText(drivingLicenceExpireDate)
                    etBusNumb.setText(busNumber)
                    etBusRoute.setText(busRoute)
                    etEmail.setText(email)
                    etPassword.setText(password)

                    btnSave.text = "Update Driver"

                }


            }
        }


    }

    override fun setListener() {

        mViewBinding.apply {

            btnSave.setOnClickListener {

                when {
                    isEmpty(etName) -> {
                        etName.error = "Name is required!"
                        etName.requestFocus()
                    }
                    isEmpty(etDob) -> {
                        etDob.error = "D-O-B is required!"
                        etDob.requestFocus()
                    }
                    isEmpty(etExperience) -> {
                        etExperience.error = "Experience is required!"
                        etExperience.requestFocus()
                    }
                    isEmpty(etNumber) -> {
                        etNumber.error = "Contact number is required!"
                        etNumber.requestFocus()
                    }
                    isEmpty(etDrivingLicenceNumber) -> {
                        etDrivingLicenceNumber.error = "Driving Licence number is required!"
                        etDrivingLicenceNumber.requestFocus()
                    }
                    isEmpty(etDrivingLicenceNumber) -> {
                        etDrivingLicenceNumber.error = "Driving Licence expire date is required!"
                        etDrivingLicenceNumber.requestFocus()
                    }
                    isEmpty(etBusNumb) -> {
                        etBusNumb.error = "Bus Number is required!"
                        etBusNumb.requestFocus()
                    }
                    isEmpty(etBusRoute) -> {
                        etBusRoute.error = "Bus Route is required!"
                        etBusRoute.requestFocus()
                    }
                    isEmpty(etEmail) -> {
                        etEmail.error = "Email is required!"
                        etEmail.requestFocus()
                    }
                    isEmailValid(getText(etEmail)) -> {
                        etEmail.error = "Email is not valid!"
                        etEmail.requestFocus()
                    }

                    isEmpty(etPassword) -> {
                        etPassword.error = "Password is required!"
                        etPassword.requestFocus()
                    }
                    else -> {
                        if (driverData != null) {
                            uploadDataToServer(driverData!!.uid!!, true)
                        } else {
                            saveDriverData()
                        }


                    }
                }
            }
            etDob.setOnClickListener { Utils.selectDate(this@AddNewDriverActivity, etDob) }
            etDrivingLicenceExpireDate.setOnClickListener {
                Utils.selectDate(
                    this@AddNewDriverActivity,
                    etDrivingLicenceExpireDate
                )
            }
        }
    }

    private fun saveDriverData() {

        mViewBinding.apply {
            setVisibility(pb, btnSave)
            auth.createUserWithEmailAndPassword(
                getText(etEmail),
                getText(etPassword)
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser.uid
                    uploadDataToServer(uid, false)
                } else {
                    setVisibility(btnSave, pb)
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
                        showSnackBar(
                            e.message!!
                        )
                    }
                }
            }

        }


    }

    private fun uploadDataToServer(uid: String, isEdit: Boolean) {

        mViewBinding.apply {
            Utils.apply {

                setVisibility(btnSave, pb)

                val driver = Driver(
                    uid,
                    getText(etName),
                    getText(etDob),
                    getText(etExperience),
                    getText(etNumber),
                    getText(etDrivingLicenceNumber),
                    getText(etDrivingLicenceExpireDate),
                    getText(etBusNumb),
                    getText(etBusRoute),
                    getText(etEmail),
                    getText(etPassword),
                )
                if (isEdit) {

                    DRIVER_DB_REF.child(uid).setValue(driver)
                    showSnackBar("Driver Updated Successfully")
                    onBackPressed()
                } else {


                    /*     FirebaseDatabase.getInstance()
                             .getReference(DataBase.DRIVER_DB_REF)
                             .child(uid)
                             .setValue(user)*/

                    DRIVER_DB_REF.child(uid).setValue(driver)
                    showSnackBar("Driver Added Successfully")
                    onBackPressed()
                }


            }
        }


    }

    companion object {
        private const val TAG = "AddNewDriverActivity"
    }
}