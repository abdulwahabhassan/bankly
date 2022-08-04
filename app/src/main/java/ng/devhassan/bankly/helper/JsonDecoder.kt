package ng.devhassan.bankly.helper

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import ng.devhassan.bankly.model.Transaction
import javax.inject.Inject

class JsonDecoder @Inject constructor(private val context: Context, private val moshi: Moshi) {

    fun loadTransactionsFromAsset(
        jsonFileName: String
    ): List<Transaction>? {
        val data: List<Transaction>?
        try {
            val stream = context.assets.open(jsonFileName)
            val size = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            val transactionListType = Types.newParameterizedType(
                List::class.java, Transaction::class.java
            )
            val adapter: JsonAdapter<List<Transaction>> = moshi.adapter(transactionListType)
            data = adapter.fromJson(String(buffer, charset("UTF-8")))
            println("$data")
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return data
    }

}