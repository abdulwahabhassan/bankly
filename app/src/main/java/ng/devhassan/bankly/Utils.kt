package ng.devhassan.bankly

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
}