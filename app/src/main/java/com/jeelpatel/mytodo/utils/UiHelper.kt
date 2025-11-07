package com.jeelpatel.mytodo.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.jeelpatel.mytodo.R
import java.text.SimpleDateFormat
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
        return SimpleDateFormat("dd-MMM-yyyy, hh:mm a", Locale.getDefault())
            .format(Date(millis))
    }

    /** ✅ Convert string → millis */
    fun parseDateToMillis(dateStr: String): Long {
        return try {
            SimpleDateFormat("dd-MMM-yyyy, hh:mm a", Locale.getDefault())
                .parse(dateStr)?.time ?: 0L
        } catch (e: Exception) {
            0L
        }
    }
}
