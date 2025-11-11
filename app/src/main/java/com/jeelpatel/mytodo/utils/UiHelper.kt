package com.jeelpatel.mytodo.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object UiHelper {

    /** ✅ Normal Toast */
    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    /** ✅ Snackbar with BottomNav anchor */
    fun showSnackWithBottomNav(
        view: View,
        msg: String,
    ) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
            .setAction("Done") {}
            .show()
    }

    /** ✅ Convert dateMillis → formatted string */
    fun formatDate(millis: Long): String {
        return SimpleDateFormat(Config.DATE_TIME_FORMAT, Locale.getDefault())
            .format(Date(millis))
    }

    /** ✅ Convert string → millis */
    fun parseDateToMillis(dateStr: String): Long {
        return try {
            SimpleDateFormat(Config.DATE_TIME_FORMAT, Locale.getDefault())
                .parse(dateStr)?.time ?: 0L
        } catch (_: Exception) {
            0L
        }
    }

    fun showMaterialDateTimePicker(
        fragmentManager: FragmentManager,
        onDateTimeSelected: (String) -> Unit
    ) {

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build()

        datePicker.addOnPositiveButtonClickListener { date ->
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = date

            val timePicker = MaterialTimePicker.Builder()
                .setTitleText("Select time")
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                .setMinute(calendar.get(Calendar.MINUTE))
                .build()

            timePicker.addOnPositiveButtonClickListener {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
                calendar.set(Calendar.MINUTE, timePicker.minute)

                onDateTimeSelected(
                    formatDate(calendar.timeInMillis)
                )
            }

            timePicker.show(fragmentManager, "M3_TIME_PICKER")
        }

        datePicker.show(fragmentManager, "M3_DATE_PICKER")
    }

}
