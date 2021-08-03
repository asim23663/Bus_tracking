package com.uni.onclicklgubus.sharedPref

import com.uni.onclicklgubus.model.Driver
import com.uni.onclicklgubus.model.Student
import com.uni.onclicklgubus.model.User


class SharedPrefHelper {
    //login preferences
    fun saveDeviceToken(value: String?) {
        SharedPref.instance!!.saveValue(DEVICE_TOKEN, value)
    }

    val deviceToken: String
        get() = SharedPref.instance!!.getStringValue(DEVICE_TOKEN)!!

    fun saveIntegerValue(value: Int) {
        SharedPref.instance!!.saveValue(ABC_VALUE, value)
    }

    val integerValue: Int
        get() = SharedPref.instance!!.getIntValue(ABC_VALUE)

//    fun saveGuestLogin(isTrue: Boolean) {
//        SharedPref.instance!!.saveValue(Constants.API.GUEST_LOGIN, isTrue)
//    }

    //Guest Login preferences
//    val isGuestLoggedIn: Boolean
//        get() = SharedPref.instance!!.getBoolValue(Constants.API.GUEST_LOGIN)


    fun saveAdminLogin(isTrue: Boolean) {
        SharedPref.instance!!.saveValue(IS_ADMIN, isTrue)
    }

    val isAdminLogin: Boolean
        get() = SharedPref.instance!!.getBoolValue(IS_ADMIN)

    fun saveStudentLogin(isTrue: Boolean) {
        SharedPref.instance!!.saveValue(IS_STUDENT, isTrue)
    }

    val isStudentLogin: Boolean
        get() = SharedPref.instance!!.getBoolValue(IS_STUDENT)

    fun saveDriverLogin(isTrue: Boolean) {
        SharedPref.instance!!.saveValue(IS_DRIVER, isTrue)
    }

    val isDriverLogin: Boolean
        get() = SharedPref.instance!!.getBoolValue(IS_DRIVER)


    fun saveDriverData(driver: Driver) {
        SharedPref.instance!!.saveDriverData(LOGIN_DATA, driver);
    }

    val getDriverData: Driver
        get() = SharedPref.instance!!.getDriverData(LOGIN_DATA)


    fun saveStudentData(student: Student) {
        SharedPref.instance!!.saveStudentData(LOGIN_DATA, student);
    }

    val getStudentData: Student
        get() = SharedPref.instance!!.getStudentData(LOGIN_DATA)

    companion object {
        //add key here
        private const val LOGIN_TYPE = "LOGIN_TYPE"
        private const val ABC_VALUE = "ABC_VALUE"
        private const val IS_ADMIN = "isAdmin"
        private const val IS_STUDENT = "isStudent"
        private const val IS_DRIVER = "isDriver"
        private const val LOGIN_DATA = "LOGIN_DATA"
        private const val DEVICE_TOKEN = "DEVICE_TOKEN"
        var instance: SharedPrefHelper? = null
            get() {
                if (field == null) {
                    synchronized(SharedPrefHelper::class.java) {
                        if (field == null) {
                            field = SharedPrefHelper()
                        }
                    }
                }
                return field
            }
            private set
    }
}