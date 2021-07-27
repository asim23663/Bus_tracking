package com.uni.onclicklgubus.base

import android.app.Activity
import android.content.Intent
import android.graphics.Color.red
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.uni.onclicklgubus.MyApp
import com.uni.onclicklgubus.R

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var mViewBinding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewBinding = getViewBinding()
    }

    abstract fun getViewBinding(): VB
    abstract fun init()
    abstract fun setListener()

    fun changeStatusBarColor(statusBarColor: Int, myActivityReference: Activity) {
        val window = myActivityReference.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = statusBarColor
    }


    fun showSnackBarError(msg: String?) {
        Snackbar.make(mViewBinding.root.rootView, msg!!, Snackbar.LENGTH_SHORT)
            .setAction("Ok") {
            }
            .setBackgroundTint(MyApp.context.resources.getColor(R.color.red, null))
            .show()
    }

    fun isEmailValid(email: String?): Boolean {
        return !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showSnackBar(msg: String?) {
        Snackbar.make(mViewBinding.root.rootView, msg!!, Snackbar.LENGTH_SHORT)
            .show()
    }

    fun navigateAndClearBackStack(cls: Class<*>?) {
        val intent = Intent(this, cls)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    fun navigateToNextActivity(cls: Class<*>?) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }

    fun getText(et: EditText): String {
        return et.text.toString().trim { it <= ' ' }
    }

    fun getText(et: AutoCompleteTextView): String {
        return et.text.toString().trim { it <= ' ' }
    }

    fun getText(tv: TextView): String {
        return tv.text.toString().trim { it <= ' ' }
    }

    fun getText(button: MaterialButton): String {
        return button.text.toString().trim { it <= ' ' }
    }

    fun setVisibility(visible: View, gone: View) {
        visible.visibility = View.VISIBLE
        gone.visibility = View.GONE
    }

    fun isEmpty(tv: TextView): Boolean {
        return TextUtils.isEmpty(getText(tv).trim { it <= ' ' })
    }

    fun isEmpty(et: EditText): Boolean {
        return TextUtils.isEmpty(getText(et))
    }

    fun isEmpty(et: TextInputEditText): Boolean {
        return TextUtils.isEmpty(getText(et))
    }

    fun isEmpty(et: AutoCompleteTextView): Boolean {
        return TextUtils.isEmpty(getText(et))
    }

    fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}
