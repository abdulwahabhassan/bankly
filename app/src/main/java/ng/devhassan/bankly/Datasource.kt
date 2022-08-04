package ng.devhassan.bankly

import javax.inject.Inject

class Datasource @Inject constructor(private val jsonDecoder: JsonDecoder) {
    fun fetchTransactions(): List<Transaction>? {
        return jsonDecoder.loadTransactionsFromAsset("transactions-data.json")
    }
}