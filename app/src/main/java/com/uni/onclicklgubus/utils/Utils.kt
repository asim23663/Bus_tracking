package com.uni.onclicklgubus.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.library.BuildConfig.DEBUG
import coil.load
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.uni.onclicklgubus.MyApp.Companion.context
import com.uni.onclicklgubus.R
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException
import java.io.InputStream
import java.math.BigDecimal
import java.nio.charset.StandardCharsets
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object Utils {


    @SuppressLint("SimpleDateFormat")
    fun getCurrentMonth(): String {
        val dateFormat: DateFormat = SimpleDateFormat("MMMM")
        val date = Date()
//        Log.d("Month", dateFormat.format(date))

        return dateFormat.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentYear(): String {

        val dateFormat: DateFormat = SimpleDateFormat("yyyy")
        val date = Date()

        return dateFormat.format(date)
//        return Calendar.getInstance().get(Calendar.YEAR);
    }

    @SuppressLint("SimpleDateFormat")
    fun changeDateFormat(date: String?): String? {
//        String date="Mar 10, 2016 6:30:00 PM";
        var date = date
        @SuppressLint("SimpleDateFormat") var spf = SimpleDateFormat(DATE_FORMAT)
        val newDate: Date?
        return try {
            newDate = spf.parse(date)
            spf = SimpleDateFormat("dd MMM yyyy")
            if (DEBUG && newDate == null) {
                error("Assertion failed")
            }
            date = spf.format(newDate)
            date
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }

    fun sharePost(ct: Context?, postLink: String) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, ct!!.resources.getString(R.string.app_name))
            var shareMessage = "\nCheck out this interesting post\n\n"
            shareMessage =
                """${shareMessage}$postLink
                
                Trumpetmedia.org
                 """.trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            ct.startActivity(Intent.createChooser(shareIntent, "Share via"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun goToNextScreenWithFinish(cls: Class<*>?, ct: Context) {
        val intent = Intent(ct, cls)
        ct.startActivity(intent)
        (ct as Activity).finish()
    }



    fun goToNextScreenWithoutFinish(cls: Class<*>?, ct: Context) {
        val intent = Intent(ct, cls)
        ct.startActivity(intent)
    }

        const val DATE_FORMAT = "E MMM d, yyyy"
//    const val DATE_FORMAT = "yyyy-MM-dd"

    @SuppressLint("SimpleDateFormat")
    var dateFormat = SimpleDateFormat("E MMM d, yyyy")
    fun getJsonFromAssets(context: Context, fileName: String?): String? {
        return try {
            val `is` = context.assets.open(fileName!!)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, StandardCharsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    private fun formatTimeStamp(time: Long): String? {
        var time = time
        val SECOND_MILLIS = 1000
        val MINUTE_MILLIS = 60 * SECOND_MILLIS
        val HOUR_MILLIS = 60 * MINUTE_MILLIS
        val DAY_MILLIS = 24 * HOUR_MILLIS
        if (time < 1000000000000L) {
            time *= 1000
        }
        val now = System.currentTimeMillis()
        if (time > now || time <= 0) {
            return null
        }
        val diff = now - time
        return when {
            diff < MINUTE_MILLIS -> {
                "Just now"
            }
            diff < 2 * MINUTE_MILLIS -> {
                "A minute ago"
            }
            diff < 50 * MINUTE_MILLIS -> {
                "$diff / $MINUTE_MILLIS minutes ago"
            }
            diff < 90 * MINUTE_MILLIS -> {
                "An hour ago"
            }
            diff < 24 * HOUR_MILLIS -> {
                "$diff / $HOUR_MILLIS hours ago"
            }
            diff < 48 * HOUR_MILLIS -> {
                "Yesterday"
            }
            else -> {
                "$diff / $DAY_MILLIS  days ago"
            }
        }
    }

    fun getBitmapFromAsset(strName: String?): Bitmap {
        val assetManager = context.assets
        var istr: InputStream? = null
        try {
            istr = assetManager.open(strName!!)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return BitmapFactory.decodeStream(istr)
    }

    fun makeACall(ct: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        ct.startActivity(intent)
    }




    fun navigateToDirection(ct: Context, currentLatLng: LatLng, destinationLatLng: LatLng) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.com/maps?saddr=" + currentLatLng.latitude + "," + currentLatLng.longitude + "&daddr=" + destinationLatLng.latitude + "," + destinationLatLng.longitude)
        )
        ct.startActivity(intent)
    }

    //Formatting TimeStamp to 'EEE MMM dd yyyy (HH:mm:ss)'
    //Input  : 2018-05-23 9:59:01
    //Output : Wed May 23 2018 (9:59:01)
    fun formatDate(dateStr: String?): String {
        try {
//            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Mon Dec 21, 2020
            @SuppressLint("SimpleDateFormat")
//            val fmt = SimpleDateFormat("EEE MMM dd, yyyy")
//            val fmt = SimpleDateFormat("EEE MMM dd, yyyy")
            val fmt = SimpleDateFormat("yyyy-MM-dd")
            val date = fmt.parse(dateStr)
            @SuppressLint("SimpleDateFormat")
            val fmtOut = SimpleDateFormat("MMM dd yyyy")
            return fmtOut.format(date)
        } catch (e: ParseException) {
        }
        return ""
    }

    fun getMonth(dateStr: String): String {
        try {
            @SuppressLint("SimpleDateFormat")
            val fmt = SimpleDateFormat("MMM yyyy")
            val date = fmt.parse(dateStr)
            @SuppressLint("SimpleDateFormat")
            val fmtOut = SimpleDateFormat("MM")
            return fmtOut.format(date)
        } catch (e: ParseException) {
        }
        return ""
    }

    fun getYear(dateStr: String): String {
        try {
            @SuppressLint("SimpleDateFormat")
            val fmt = SimpleDateFormat("MMM yyyy")
            val date = fmt.parse(dateStr)
            @SuppressLint("SimpleDateFormat")
            val fmtOut = SimpleDateFormat("yyyy")
            return fmtOut.format(date)
        } catch (e: ParseException) {
        }
        return ""
    }

    fun reverseDateFormat(dateStr: String?): String {
        try {
//            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Mon Dec 21, 2020
            @SuppressLint("SimpleDateFormat") val fmt = SimpleDateFormat("yyyy-MM-dd")
            val date = fmt.parse(dateStr)
            @SuppressLint("SimpleDateFormat") val fmtOut = SimpleDateFormat("EEE MMM dd, yyyy")
            return fmtOut.format(date)
        } catch (e: ParseException) {
        }
        return ""
    }

    fun roundDecimal(d: Float, decimalPlace: Int): BigDecimal {
        var bd = BigDecimal(d.toString())
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP)
        return bd
    }



    val currentDate: String
        @SuppressLint("SimpleDateFormat")
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
            current.format(formatter)
        } else {
            val date = Date()
            val formatter = SimpleDateFormat(DATE_FORMAT)
            formatter.format(date)
        }

    //2020-05-09
    fun selectDate(ct: Context?, etDate: MaterialButton) {
        val myCalendar = Calendar.getInstance()
        val date =
            OnDateSetListener { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                // TODO Auto-generated method stub
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = monthOfYear
                myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                updateLabel(etDate, myCalendar)
            }
        android.app.DatePickerDialog(
            ct!!, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        ).show()
    }

    fun selectDate(ct: Context?, etDate: TextInputEditText) {
        val myCalendar = Calendar.getInstance()
        val date =
            OnDateSetListener { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                // TODO Auto-generated method stub
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = monthOfYear
                myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                updateLabel(etDate, myCalendar)
            }
        android.app.DatePickerDialog(
            ct!!, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        ).show()
    }

    fun selectDate(ct: Context?, tvDate: TextView) {
        val myCalendar = Calendar.getInstance()
        val date =
            OnDateSetListener { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                // TODO Auto-generated method stub
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = monthOfYear
                myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                updateLabel(tvDate, myCalendar)
            }
        android.app.DatePickerDialog(
            ct!!, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        ).show()
    }

    fun selectTime(ct: Context?, tvTime: TextView) {
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
        val minute = mcurrentTime[Calendar.MINUTE]
        val mTimePicker: TimePickerDialog = TimePickerDialog(
            ct,
            { _, selectedHour, selectedMinute -> tvTime.text = "$selectedHour:$selectedMinute" },
            hour,
            minute,
            true
        ) //Yes 24 hour time

        mTimePicker.show()
    }


    fun isInternetAvailable(): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }

    fun notInternetDialog(c: Context?): AlertDialog.Builder {
        val builder: AlertDialog.Builder = AlertDialog.Builder(c!!)
        builder.setTitle("No Internet Connection")
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit")
        builder.setPositiveButton("Ok"
        ) { dialog, _ -> dialog.cancel() }
        return builder
    }


    fun updateLabel(et: MaterialButton, myCalendar: Calendar) {
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.US)
        et.text = sdf.format(myCalendar.time)
    }

    fun updateLabel(tv: TextView, myCalendar: Calendar) {
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.US)
        tv.text = sdf.format(myCalendar.time)
    }


    fun navigateAndClearBackStack(cls: Class<*>?, ct: Context) {
        val intent = Intent(ct, cls)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        ct.startActivity(intent)
        (ct as Activity).finish()
    }

    fun showSnackBar(view: View, msg: String?) {
        //findViewById(android.R.id.content)
        Snackbar.make(view, msg!!, Snackbar.LENGTH_SHORT)
            .show()
    }



    fun setVisibility(visible: View, gone: View) {
        visible.visibility = View.VISIBLE
        gone.visibility = View.GONE
    }

    fun showToast(msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    fun shareApp(ct: Context?) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT,
                ct!!.resources.getString(R.string.app_name))
            var shareMessage = "\nLet me recommend you this application\n\n"
            shareMessage =
                """
                  ${shareMessage}https://play.google.com/store/apps/details?id=${context.packageName}
                  
                  """.trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            ct.startActivity(Intent.createChooser(shareIntent, "Choose one"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Suppress("DEPRECATION")
    fun getSpannedText(text: String): Spanned? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
        } else {
            Html.fromHtml(text);
        }
    }

    fun showMaterialDialog(context: Context, title: String?, message: String?) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(context.resources.getString(R.string.ok)) { dialog, which ->
                // Respond to positive button press
                dialog.dismiss()
            }
            .show()
    }


    fun showShortToast(msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }



    fun setImageWithCoil(
        imgUrl: String?,
        ivImage: ShapeableImageView?,
    ) {
        ivImage!!.load(imgUrl) {
            target(
                onStart = { placeholder ->
                    // Handle the placeholder drawable.
                },
                onSuccess = { result ->
                    ivImage.load(result)
                    // Handle the successful result.
                },
                onError = { error ->
                    ivImage.load(R.drawable.ic_place_holder)

                    // Handle the error drawable.
                }
            )

        }
    }

    fun setImageWithCoil(
        imgUrl: String?,
        ivImage: CircleImageView?,
        placeHolder: Int,
    ) {
        ivImage!!.load(imgUrl) {
            target(
                onStart = { placeholder ->
                    // Handle the placeholder drawable.
                },
                onSuccess = { result ->
                    ivImage.load(result)
                    // Handle the successful result.
                },
                onError = { error ->
                    ivImage.load(placeHolder)

                    // Handle the error drawable.
                }
            )
        }
    }





}