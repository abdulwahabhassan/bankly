package ng.devhassan.bankly

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class TransactionsRepository @Inject constructor(private val datasource: Datasource) {
    fun getTransactions(): List<Transaction>? {
        return datasource.fetchTransactions()
    }
}