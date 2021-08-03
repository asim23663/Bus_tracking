package com.uni.onclicklgubus.sharedPref

import android.content.Context
import com.google.gson.Gson
import com.uni.onclicklgubus.MyApp
import com.uni.onclicklgubus.model.Driver
import com.uni.onclicklgubus.model.Student
import com.uni.onclicklgubus.model.User


class SharedPref
private constructor() {
    private val sharedPreferences = MyApp.context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()
    fun saveValue(key: String?, value: String?) {
        editor.putString(key, value)
        editor.apply()
    }

    fun saveValue(key: String?, value: Int) {
        editor.putInt(key, value)
        editor.apply()
    }

    fun saveValue(key: String?, value: Boolean) {
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getStringValue(key: String?): String? {
        return sharedPreferences.getString(key, "")
    }

    fun getIntValue(key: String?): Int {
        return sharedPreferences.getInt(key, -1)
    }

    fun clearSharedPref() {
        editor.clear().apply()
    }

    fun getBoolValue(key: String?): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun saveLoginData(key: String?, user: User) {
        val gson = Gson()
        val json = gson.toJson(user)
        editor.putString(key, json);
        editor.apply();
    }

    fun saveDriverData(key: String?, user: Driver) {
        val gson = Gson()
        val json = gson.toJson(user)
        editor.putString(key, json);
        editor.apply();
    }

    fun saveStudentData(key: String?, user: Student) {
        val gson = Gson()
        val json = gson.toJson(user)
        editor.putString(key, json);
        editor.apply();
    }

    fun getLoginData(key: String?): User {
        val gson = Gson()
        val json = sharedPreferences.getString(key, null)
        return gson.fromJson(json, User::class.java)

    }

    fun getDriverData(key: String?): Driver {
        val gson = Gson()
        val json = sharedPreferences.getString(key, null)
        return gson.fromJson(json, Driver::class.java)

    }

    fun getStudentData(key: String?): Student {
        val gson = Gson()
        val json = sharedPreferences.getString(key, null)
        return gson.fromJson(json, Student::class.java)

    }

    companion object {
        private const val SHARED_PREF = "OnClickLGU"
        private var mInstance: SharedPref? = null

        @get:Synchronized
        val instance: SharedPref?
            get() {
                if (mInstance == null) {
                    mInstance = SharedPref()
                }
                return mInstance
            }
    }
}