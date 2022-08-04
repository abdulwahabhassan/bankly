package ng.devhassan.bankly.datasource

import ng.devhassan.bankly.helper.JsonDecoder
import ng.devhassan.bankly.model.Transaction
import javax.inject.Inject

class Datasource @Inject constructor(private val jsonDecoder: JsonDecoder) {
    fun fetchTransactions(): List<Transaction>? {
        return jsonDecoder.loadTransactionsFromAsset("transactions-data.json")
    }
}