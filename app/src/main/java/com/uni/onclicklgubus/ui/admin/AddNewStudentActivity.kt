package com.uni.onclicklgubus.ui.admin

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.uni.onclicklgubus.R
import com.uni.onclicklgubus.base.BaseActivity
import com.uni.onclicklgubus.databinding.ActivityAddNewStudentBinding
import com.uni.onclicklgubus.firebase.DataBase
import com.uni.onclicklgubus.model.Driver
import com.uni.onclicklgubus.model.Student
import com.uni.onclicklgubus.utils.Constants
import com.uni.onclicklgubus.utils.Utils
import kotlinx.coroutines.delay

class AddNewStudentActivity : BaseActivity<ActivityAddNewStudentBinding>() {

    private lateinit var auth: FirebaseAuth
    private var studentData: Student? = null

    override fun getViewBinding(): ActivityAddNewStudentBinding =
        ActivityAddNewStudentBinding.inflate(layoutInflater)

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
            val items = listOf("Male", "Female", "Other")
            val adapter = ArrayAdapter(this@AddNewStudentActivity, R.layout.list_item, items)
            (etGender as? AutoCompleteTextView)?.setAdapter(adapter)


            if (intent.extras != null) {
                supportActionBar!!.title = "Edit Student";
                studentData = intent.getSerializableExtra(Constants.BUNDLE_DATA) as Student
                studentData!!.apply {
                    etName.setText(name)
                    etDob.setText(dob)
                    etFName.setText(fatherName)
                    etGender.setText(gender)
                    etPAddress.setText(permanentAddress)
                    etCAddress.setText(currentAddress)
                    etBatch.setText(batch)
                    etDepartment.setText(department)
                    etRollNumber.setText(rollNumber)
                    etEmail.setText(email)
                    etPassword.setText(password)
                    etContactNumber.setText(contactNumber)

                    btnSave.text = "Update Student"

                }


            }

        }

    }

    override fun setListener() {
        mViewBinding.apply {
            etDob.setOnClickListener {
                Utils.selectDate(this@AddNewStudentActivity, etDob)
//            selectCalendarDate()
            }

            btnSave.setOnClickListener {

                when {
                    isEmpty(etName) -> {
                        etName.error = "Name is required!"
                        etName.requestFocus()
                    }
                    isEmpty(etFName) -> {
                        etFName.error = "Father Name required!"
                        etFName.requestFocus()
                    }
                    isEmpty(etDob) -> {
                        etDob.error = "D-O-B is required!"
                        etDob.requestFocus()
                    }
                    isEmpty(etGender) -> {
                        etGender.error = "Gender is required!"
                        etGender.requestFocus()
                    }
                    isEmpty(etPAddress) -> {
                        etPAddress.error = "Permanent Address is required!"
                        etPAddress.requestFocus()
                    }
                    isEmpty(etCAddress) -> {
                        etCAddress.error = "Curret Address is required!"
                        etCAddress.requestFocus()
                    }
                    isEmpty(etContactNumber) -> {
                        etContactNumber.error = "Contact Number is required!"
                        etContactNumber.requestFocus()
                    }
                    isEmpty(etBatch) -> {
                        etBatch.error = "Batch is required!"
                        etBatch.requestFocus()
                    }
                    isEmpty(etDepartment) -> {
                        etDepartment.error = "Department is required!"
                        etDepartment.requestFocus()
                    }
                    isEmpty(etRollNumber) -> {
                        etRollNumber.error = "Roll Number is required!"
                        etRollNumber.requestFocus()
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

                        if (studentData != null) {
                            uploadDataToServer(studentData!!.uid!!, true)
                        } else {
                            saveStudentData()
                        }
                    }
                }
            }
        }
    }

    private fun saveStudentData() {
        mViewBinding.apply {
            setVisibility(pb, btnSave)
            auth.createUserWithEmailAndPassword(
                getText(etEmail),
                getText(etPassword)
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser.uid
                    uploadDataToServer(uid,false)
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
                        Log.e(Companion.TAG, e.message!!)
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
                val student = Student(
                    uid,
                    getText(etName),
                    getText(etFName),
                    getText(etDob),
                    getText(etGender),
                    getText(etPAddress),
                    getText(etCAddress),
                    getText(etContactNumber),
                    getText(etBatch),
                    getText(etDepartment),
                    getText(etRollNumber),
                    getText(etEmail),
                    getText(etPassword)
                )
                DataBase.STUDENT_DB_REF.child(uid).setValue(student)

                if (isEdit){
                    showSnackBar("Student Updated Successfully")
                    onBackPressed()
                }else{
                    showSnackBar("Student Added Successfully")
                    onBackPressed()
                }


            }
        }
    }

    companion object {
        private const val TAG = "AddNewStudentActivity"
    }
}