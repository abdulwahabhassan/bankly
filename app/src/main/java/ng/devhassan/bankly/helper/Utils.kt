package ng.devhassan.bankly.helper

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun formatAmount(value: Any): String {
        val valueToBeFormatted: Number = if (value is String) {
            value.toDouble()
        } else {
            value as Number
        }
        val df = DecimalFormat("##,###,##0.00")
        return df.format(valueToBeFormatted)
    }

    fun formatTransactionDate(date: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formatter = SimpleDateFormat("dd, MMM yyyy, hh:mm:ss aa", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("Africa/Lagos")
        return formatter.format(
            parser.parse(
                date.replace('T', ' ').substringBefore('.')
            ) ?: ""
        )
    }

    fun hideKeyboard(context: Context?, view: View?) {
        if (context != null) {
            val imm =
                context.applicationContext.getSystemService(
                    Context.INPUT_METHOD_SERVICE
                ) as InputMethodManager
            if (view != null) {
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }
}